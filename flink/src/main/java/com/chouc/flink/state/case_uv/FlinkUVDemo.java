package com.chouc.flink.state.case_uv;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

import java.time.Duration;
import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkUVDemo
 * @Package com.chouc.flink.state.case_uv
 * @Description:
 * @date 2021/4/1
 */
public class FlinkUVDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("","");
        FlinkKafkaConsumer kafkaSource = new FlinkKafkaConsumer("test",new SimpleStringSchema(),properties);
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("group.id","kafka_group_id");
        properties.setProperty(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "10");

        kafkaSource.setStartFromEarliest();
        kafkaSource.setCommitOffsetsOnCheckpoints(true);

        kafkaSource.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)));

        DataStreamSource<String> dataStreamSource = env.addSource(kafkaSource);
        dataStreamSource.map(new MapFunction<String, Tuple2<String,String>>() {
            @Override
            public Tuple2<String, String> map(String value) throws Exception {
                String[] values = value.split(",");
                return Tuple2.of(values[0],values[1]);
            }
        });
    }
}
