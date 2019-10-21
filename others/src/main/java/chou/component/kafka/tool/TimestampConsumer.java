//package chou.component.kafka.tool;
//
///**
// * @author chouc
// * @version V1.0
// * @Title: TimestampConsumer
// * @Package chou.component.kafka.tool
// * @Description:
// * @date 10/17/19
// */
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
//import org.apache.kafka.common.PartitionInfo;
//import org.apache.kafka.common.TopicPartition;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// *  Kafka 新版消费者基于时间戳索引消费消息
// */
//public class TimestampConsumer {
//
//    public static void main(String[] args) {
//
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "192.168.0.235:9092,192.168.0.237:9092,192.168.0.229:9092");
//        props.put("group.id", "sparkle-nginx-flow");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        String topic = "ddos";
//
//        try {
//            // 获取topic的partition信息
//            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
//            List<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
//
//            Map<TopicPartition, Long> timestampsToSearch = new HashMap<TopicPartition, Long>();
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date now = new Date();
//            long nowTime = now.getTime();
//            System.out.println("当前时间: " + df.format(now));
//            long fetchDataTime = nowTime - 1000 * 60 * 1;  // 计算1分钟之前的时间戳
//
//            for(PartitionInfo partitionInfo : partitionInfos) {
//                topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
//                timestampsToSearch.put(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()), fetchDataTime);
//            }
//
//            consumer.assign(topicPartitions);
//
//            // 获取每个partition一个小时之前的偏移量
//            Map<TopicPartition, OffsetAndTimestamp> map = consumer.offsetsForTimes(timestampsToSearch);
//
//            OffsetAndTimestamp offsetTimestamp = null;
//            System.out.println("开始设置各分区初始偏移量...");
//            for(Map.Entry<TopicPartition, OffsetAndTimestamp> entry : map.entrySet()) {
//                // 如果设置的查询偏移量的时间点大于最大的索引记录时间，那么value就为空
//                offsetTimestamp = entry.getValue();
//                if(offsetTimestamp != null) {
//                    int partition = entry.getKey().partition();
//                    long timestamp = offsetTimestamp.timestamp();
//                    long offset = offsetTimestamp.offset();
//                    System.out.println("partition = " + partition +
//                            ", time = " + df.format(new Date(timestamp))+
//                            ", offset = " + offset);
//                    // 设置读取消息的偏移量
//                    consumer.seek(entry.getKey(), offset);
//                }
//            }
//            System.out.println("设置各分区初始偏移量结束...");
//
//            while(true) {
//                ConsumerRecords<String, String> records = consumer.poll(1000);
//                for (ConsumerRecord<String, String> record : records) {
//                    System.out.println("partition = " + record.partition() + ", offset = " + record.offset() + " v =" + record.value());
//                    System.out.println("--------");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            consumer.close();
//        }
//    }
//}
