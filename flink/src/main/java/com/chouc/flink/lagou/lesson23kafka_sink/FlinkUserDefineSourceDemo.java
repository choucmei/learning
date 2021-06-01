package com.chouc.flink.lagou.lesson23kafka_sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: Demo
 * @Package com.chouc.flink.lagou.lession23kafka_sink
 * @Description:
 * @date 7/30/20
 */
public class FlinkUserDefineSourceDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.enableCheckpointing(5000);
        DataStreamSource source = env.addSource(new UserDefineSource());
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                "127.0.0.1:9092", //broker 列表
                "flink_learning",           //topic
                new SimpleStringSchema()); // 消息序列化
        //写入 Kafka 时附加记录的事件时间戳
        producer.setWriteTimestampToKafka(true);
        source.addSink(producer);
        env.execute("kafka sink");
    }
}
