package chou.component.kafka.tool;

import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.OffsetMetadataAndError;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.network.BlockingChannel;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author chouc
 * @version V1.0
 * @Title: KafkaOffsetTools
 * @Package chou.component.kafka.tool
 * @Description:
 * @date 10/16/19
 */
/**
 * 动态获取kafka偏移量
 */
public class KafkaOffsetTools {

    public static void getOffset(String topic,String brokerIp,int brokerPort,String consumerGroup) {

        String clientId = UUID.randomUUID().toString();
        int correlationId = 0;
        //超时时长
        BlockingChannel channel = new BlockingChannel(brokerIp, brokerPort,
                BlockingChannel.UseDefaultBufferSize(),
                BlockingChannel.UseDefaultBufferSize(),
                5000);
        channel.connect();

        List<String> seeds = new ArrayList<>();
        seeds.add(brokerIp);
        //本类
        KafkaOffsetTools kot = new KafkaOffsetTools();
        //分区号 分区元数据
        TreeMap<Integer, PartitionMetadata> metadatas = kot.findLeader(seeds, brokerPort, topic);

        long sum = 0l;
        long sumOffset = 0l;
        long lag = 0l;
        //存放Topic和分区号
        List<TopicAndPartition> partitions = new ArrayList<>();
        //遍历分区和元数据信息
        for (Entry<Integer, PartitionMetadata> entry : metadatas.entrySet()) {
            //topic  分区的id
            int partition = entry.getKey();
            TopicAndPartition testPartition = new TopicAndPartition(topic, partition);
            partitions.add(testPartition);
        }
        OffsetFetchRequest fetchRequest = new OffsetFetchRequest(
                consumerGroup,
                partitions,
                (short) 0,
                correlationId,
                clientId);
        for (Entry<Integer, PartitionMetadata> entry : metadatas.entrySet()) {
            int partition = entry.getKey();
            try {
                channel.send(fetchRequest.underlying());
                OffsetFetchResponse fetchResponse = OffsetFetchResponse.readFrom(channel.receive().payload());
                TopicAndPartition testPartition0 = new TopicAndPartition(topic, partition);
                OffsetMetadataAndError result = fetchResponse.offsets().get(testPartition0);
                short offsetFetchErrorCode = result.error();
                if (offsetFetchErrorCode != ErrorMapping.NotCoordinatorForConsumerCode()) {
                    //恢复偏移量
                    long retrievedOffset = result.offset();
                    System.out.println(partition+"-"+result.offset());
                    sumOffset += retrievedOffset;
                }
                //获取 topic 元数据中的leader名称
                String leadBroker = entry.getValue().leader().host();
                String clientName = "Client_" + topic + "_" + partition;
                SimpleConsumer consumer = new SimpleConsumer(leadBroker, brokerPort, 100000,
                        64 * 1024, clientName);
                long readOffset = getLastOffset(consumer, topic, partition,
                        kafka.api.OffsetRequest.LatestTime(), clientName);
                sum += readOffset;
                System.out.println("partition：" + partition + "  readOffset：" + readOffset);
                if (consumer != null) consumer.close();
            } catch (Exception e) {
                channel.disconnect();
            }
        }

        System.out.println("sumLog：" + sum);
        System.out.println("offset：" + sumOffset);

        lag = sum - sumOffset;
        System.out.println("lag:" + lag);
    }

    /**
     * 获取offset
     * @param consumer
     * @param topic
     * @param partition
     * @param whichTime 要获取offset的时间,-1 最新，-2 最早  此时传的是-1
     * @param clientName
     * @return offset
     */
    public static long getLastOffset(SimpleConsumer consumer, String topic,
                                     int partition, long whichTime, String clientName) {
        //样例类 topic  和  分区
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        //创建了TopicAndPartition 作为key
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        //new PartitionOffsetRequestInfo   获取partition最新的offset
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
                whichTime, 1));
        //
        OffsetRequest request = new OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
                clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);
        if (response.hasError()) {
            System.out
                    .println("Error fetching data Offset Data the Broker. Reason: "
                            + response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }
    /**
     * @param a_seedBrokers  brokers 地址
     * @param a_port 端口
     * @param a_topic topic主题
     * @return  k 是分区id  value 是元数据的信息
     */
    private TreeMap<Integer,PartitionMetadata> findLeader(List<String> a_seedBrokers,
                                                          int a_port, String a_topic) {
        TreeMap<Integer, PartitionMetadata> map = new TreeMap<Integer, PartitionMetadata>();
        //循环 broker
        loop: for (String seed : a_seedBrokers) {
            SimpleConsumer consumer = null;
            try {
                //创建的消费者
                consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024,
                        "leaderLookup"+new Date().getTime());
                // 返回的是topic 序列  如果是 str
                // 传入的是字符串 返回的就是 list('str')  长度 就是 1
                List<String> topics = Collections.singletonList(a_topic);


                //System.out.println("topics = " + topics);
                //TopicMetadata 可以获取 broker topic 和分区
                //TopicMetadataRequest 的内容非常简单，是一个包含 TopicName 的数组，TopicMetadataResponse 则告诉使用者 Broker、Topic、Partition 的分布情况。

                //定义一个topic请求，为了获取相关topic的信息（不包括offset,有partition）
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                //发送请求获取元数据
                TopicMetadataResponse resp = consumer.send(req);
                //获取主题元数据列表
                List<TopicMetadata> metaData = resp.topicsMetadata();
                //提取主题元数据列表中指定分区的元数据信息
                for (TopicMetadata item : metaData) {
                    //把主题 分区id 最为k 分区中的元数据信息 作为 value 返回
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        map.put(part.partitionId(), part);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error communicating with Broker [" + seed
                        + "] to find Leader for [" + a_topic + ", ] Reason: " + e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        return map;
    }

    public static void main(String[] args) {
        getOffset("test_mxb","master01",9092,"test");
    }
}