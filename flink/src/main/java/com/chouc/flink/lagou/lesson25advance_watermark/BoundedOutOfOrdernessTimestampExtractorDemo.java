package com.chouc.flink.lagou.lesson25advance_watermark;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: BoundedOutOfOrdernessTimestampExtractorDemo
 * @Package com.chouc.flink.lagou.lession25advance_watermark
 * @Description:
 * @date 7/30/20
 */
public class BoundedOutOfOrdernessTimestampExtractorDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        //设置为eventtime事件类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置水印生成时间间隔100ms
//        env.getConfig().setAutoWatermarkInterval(100);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer("flink_learning", new SimpleStringSchema(), properties);
        flinkKafkaConsumer.setStartFromLatest();
        flinkKafkaConsumer.assignTimestampsAndWatermarks(WatermarkStrategy.forGenerator(context -> new WatermarkGenerator<String>() {
            private Long currentMaxTimeStamp = 0L;
            //设置允许乱序时间
            private Long maxOutOfOrderness = 5 * 1000L;

            @Override
            public void onEvent(String event, long eventTimestamp, WatermarkOutput output) {
                currentMaxTimeStamp = Math.max(eventTimestamp, currentMaxTimeStamp);
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(currentMaxTimeStamp - maxOutOfOrderness));
            }
        }).withTimestampAssigner(new SerializableTimestampAssigner<String>() {
            long timeStamp = 0L;

            @Override
            public long extractTimestamp(String s, long recordTimestamp) {
                String[] arr = s.split(",");
                try {
                    timeStamp = Long.parseLong(arr[1]) * 1000;
                    System.out.println("timeStamp:" + timeStamp + " recordTimestamp:" + recordTimestamp);
                } catch (Exception e) {
                    System.out.println(e);
                }
                return timeStamp;
            }
        }));
        DataStreamSink<String> dataStream = env.<String>addSource(flinkKafkaConsumer).
                map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String s) throws Exception {
                        String[] split = s.split(",");
                        return new Tuple2<String, Long>(split[0], Long.parseLong(split[1]));
                    }
                }).keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .aggregate(new AggregateFunction<Tuple2<String, Long>, String, String>() {
                    String accumulator = "";

                    @Override
                    public String createAccumulator() {
                        return accumulator;
                    }

                    @Override
                    public String add(Tuple2<String, Long> value, String accumulator) {
                        return accumulator + "=" + value.f0 + "-" + value.f1;
                    }

                    @Override
                    public String getResult(String accumulator) {
                        return accumulator;
                    }

                    @Override
                    public String merge(String a, String b) {
                        return a + "*" + b;
                    }
                }).print();
        env.execute("WaterMark Test Demo");
    }
}
