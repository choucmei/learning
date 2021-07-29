package com.chouc.flink.lagou.lesson24kafka_source;

import com.chouc.flink.serialization.CustomDeSerializationSchema;
import org.apache.commons.collections.map.HashedMap;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkKafkaSourceDeSerializationDemo
 * @Package com.chouc.flink.lagou.lession23kafka_sink
 * @Description:
 * @date 2020/10/26
 */
public class FlinkKafkaSourceDeSerializationDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.createLocalEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // 动态分区检测
        properties.setProperty(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "10");
//        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<>("flink_learning", new CustomDeSerializationSchema(), properties);

        // topic 规则，新建topic 不重启也会识别
        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<>(Pattern.compile("^flink_([A-Za-z0-9]*)$"), new CustomDeSerializationSchema(), properties);

        // 指定
        Map<KafkaTopicPartition, Long> offsets = new HashedMap();
        offsets.put(new KafkaTopicPartition("flink_learning", 0), 2L);
        offsets.put(new KafkaTopicPartition("flink_learning", 1), 2L);
        offsets.put(new KafkaTopicPartition("flink_learning", 2), 2L);
        consumer.setStartFromSpecificOffsets(offsets);


        consumer.setStartFromEarliest();
        DataStreamSource<ConsumerRecord<String, String>> ds = sEnv.addSource(consumer);
        ds.print();
        sEnv.execute();
    }
}
