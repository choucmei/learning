package chou.component.kafka.tool;

import kafka.api.OffsetRequest;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.client.ClientUtils;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.api.OffsetFetchRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class GetOffsetShellWrap{
    private Logger logger = LoggerFactory.getLogger(GetOffsetShellWrap.class);

    private HashMap<String, Integer> hosts;
    private int timeOut = 30000;
    private int bufferSize = 64 * 1000000;
    private String clientID ;

    public GetOffsetShellWrap(){
        init();
    }
    private void init() {
        hosts = new HashMap<>();
        hosts.put("192.168.90.97", 9092);
//        hosts.put("192.168.1.23", 9092);
//        hosts.put("192.168.1.24", 9092);
        clientID = "kafka_info_client_" + System.currentTimeMillis();
    }

    public Map<Integer,Long> getLatestOffset(String topic) {
        //kafka.api.OffsetRequest.LatestTime() = -1
        return getTopicOffset(topic,kafka.api.OffsetRequest.LatestTime());
    }

    public Map<Integer,Long> getEarliestOffset(String topic) {
        //kafka.api.OffsetRequest.EarliestTime() = -2
        return getTopicOffset(topic,kafka.api.OffsetRequest.EarliestTime());
    }


    /***
     * 获取指定 topic 的所有分区 offset
     * @param topic
     * @param whichTime   要获取offset的时间,-1 最新，-2 最早
     * @return
     */
    public Map<Integer,Long> getTopicOffset(String topic, long whichTime) {
        HashMap<Integer, Long> offsets = new HashMap<>();
        TreeMap<Integer, PartitionMetadata> leaders = this.findLeader(hosts, topic);
        leaders.forEach((part, matedata) -> {
            String leadBroker = matedata.leader().host();
            int leadPort = matedata.leader().port();
            SimpleConsumer consumer = new SimpleConsumer(leadBroker, leadPort, timeOut, bufferSize, clientID);
            long partationOffset = this.getPartationOffset(consumer, topic, part, whichTime);
            offsets.put(part,partationOffset);
        });
        return offsets;
    }


    private long getGroupParitionOffset(String topic,String groupId){
        HashMap<Integer, Long> offsets = new HashMap<>();
        TreeMap<Integer, PartitionMetadata> leaders = this.findLeader(hosts, topic);
        List<TopicAndPartition> partitions = new ArrayList<TopicAndPartition>();
        for (Map.Entry<Integer,PartitionMetadata> entry : leaders.entrySet()) {
            int partition = entry.getKey();
            TopicAndPartition testPartition = new TopicAndPartition(topic, partition);
            partitions.add(testPartition);
        }

        ZkUtils zkUtils = new ZkUtils("192.168.90.97:2181", 30000,  JaasUtils.isZkSecurityEnabled());
        OffsetFetchRequest fetchRequest = new OffsetFetchRequest(groupId, partitions, (short) 0, bufferSize, clientID);
        kafka.network.BlockingChannel channel = ClientUtils.channelToOffsetManager(groupId, zkUtils, 30000, 30000)
        channel.send(new OffsetFetchRequest("", partitions));
    }

    /***
     * 获取 offset
     * @param consumer SimpleConsumer
     * @param topic topic
     * @param partition partition
     * @param whichTime 要获取offset的时间,-1 最新，-2 最早
     * @return
     */
    private long getPartationOffset(SimpleConsumer consumer, String topic, int partition, long whichTime) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
                partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        //PartitionOffsetRequestInfo(long time, int maxNumOffsets) 中的第二个参数maxNumOffsets，没弄明白是什么意思，但是测试后发现传入1 时返回whichTime 对应的offset，传入2 返回一个包含最大和最小offset的元组

        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, OffsetRequest.CurrentVersion(), consumer.clientId());

        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            logger.error("Error fetching data Offset Data the Broker. Reason:{}",response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }

    /***
     * 获取每个 partition 元数据信息
     * @param bootstraps (host,port)
     * @param topic topic
     * @return
     */
    private TreeMap<Integer, PartitionMetadata> findLeader(Map<String, Integer> bootstraps, String topic) {
        TreeMap<Integer, PartitionMetadata> map = new TreeMap<>();
        loop:
        for (Map.Entry<String, Integer> bootstrap : bootstraps.entrySet()) {
            SimpleConsumer consumer = null;
            try {
                consumer = new SimpleConsumer(bootstrap.getKey(), bootstrap.getValue(), timeOut, bufferSize, clientID);
                List<String> topics = Collections.singletonList(topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);
                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        map.put(part.partitionId(), part);
                    }
                }
            } catch (Exception e) {
                logger.error("Error communicating with Broker [{}] to find Leader for [{}] Reason: ",bootstrap,topic,e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        return map;
    }

    public static void main(String[] args) {
        GetOffsetShellWrap getOffsetShellWrap = new GetOffsetShellWrap();
        Map<Integer,Long> result = getOffsetShellWrap.getEarliestOffset("adrequest");
        System.out.println(result);
    }
}