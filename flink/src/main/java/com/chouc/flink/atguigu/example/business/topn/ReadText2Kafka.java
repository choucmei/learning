package com.chouc.flink.atguigu.example.business.topn;

import com.chouc.flink.atguigu.example.business.topn.ClickEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class ReadText2Kafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<String> stringDataStreamSource = environment.readTextFile("C:\\Users\\chouc\\Desktop\\raw_sample.csv");
        SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = stringDataStreamSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] values = value.split(",");
                try {
                    ClickEvent clickEvent = new ClickEvent(values[0], values[2], values[3], Integer.parseInt(values[4]), Integer.parseInt(values[5]), Long.parseLong(values[1]));
                    Thread.sleep(100);
                    out.collect(clickEvent.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "s1:9092");
        FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                "s1:9092", //broker 列表
                "flink_learning",           //topic
                new SimpleStringSchema()); // 消息序列化
        //写入 Kafka 时附加记录的事件时间戳
        producer.setWriteTimestampToKafka(true);
        stringSingleOutputStreamOperator.addSink(producer);
        environment.execute();

    }
}
