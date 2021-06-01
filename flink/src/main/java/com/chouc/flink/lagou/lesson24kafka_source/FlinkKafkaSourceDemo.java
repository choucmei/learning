package com.chouc.flink.lagou.lesson24kafka_source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: Demo
 * @Package com.chouc.flink.lagou.lession24kafka_source
 * @Description:
 * @date 7/30/20
 */
public class FlinkKafkaSourceDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.enableCheckpointing(5000);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer("flink_learning", new SimpleStringSchema(), properties);
        flinkKafkaConsumer.setStartFromEarliest();

        //还可以手动指定相应的 topic, partition，offset,然后从指定好的位置开始消费
        //HashMap<KafkaTopicPartition, Long> map = new HashMap<>();
        //map.put(new KafkaTopicPartition("test", 1), 10240L);
        //假如partition有多个，可以指定每个partition的消费位置
        //map.put(new KafkaTopicPartition("test", 2), 10560L);
        //然后各个partition从指定位置消费
        //consumer.setStartFromSpecificOffsets(map);

        DataStreamSource<String> source = env.addSource(flinkKafkaConsumer);
        source.print();
        env.execute("kafka source");
    }
}
