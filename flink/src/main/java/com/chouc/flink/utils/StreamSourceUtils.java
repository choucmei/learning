package com.chouc.flink.utils;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;

public class StreamSourceUtils {

    public static DataStreamSource<String> getSocketStream(){
        return StreamExecutionEnvironment.getExecutionEnvironment().addSource(new SocketTextStreamFunction("localhost",9999,"\n",3));
    }

    public static DataStreamSource<String> getKafkaStream(){
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("s1:9092")
                .setTopics("user_behavior")
                .setGroupId("StreamSourceUtils")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        return StreamExecutionEnvironment.getExecutionEnvironment().fromSource(source, WatermarkStrategy.noWatermarks(),"kafkasource");
    }
}
