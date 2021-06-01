package com.chouc.flink.lagou.lesson08window_time_watermark;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

/**
 * @author chouc
 * @version V1.0
 * @Title: WaterMarkLaterDemo
 * @Package com.chouc.flink.lagou.lession08window_time_watermark
 * @Description:
 * @date 2020/9/8
 */
public class WaterMarkDemoV1_11 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(100);
        DataStream<Tuple2<String,Long>> ds = env.socketTextStream("127.0.0.1", 8888).map(new MapFunction<String, Tuple2<String,Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                try {
                    String[] values = value.split(",");
                    return Tuple2.of(values[0], Long.valueOf(values[1]));
                } catch (Exception e) {
                    return Tuple2.of("null", System.currentTimeMillis());
                }
            }
        });
        ds.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((SerializableTimestampAssigner<Tuple2<String, Long>>) (element, recordTimestamp) -> {
                    System.out.println(Thread.currentThread().getName()+" value :"+element.f0+" - eventime "+element.f1);
                    return element.f1 * 1000;
                })).keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .aggregate(new AggregateFunction<Tuple2<String, Long>, String, String>() {
                    String accumulator = "";
                    @Override
                    public String createAccumulator() {
                        return accumulator;
                    }

                    @Override
                    public String add(Tuple2<String, Long> value, String accumulator) {
                        return accumulator+"=" + value.f0+"-"+value.f1;
                    }

                    @Override
                    public String getResult(String accumulator) {
                        return accumulator;
                    }

                    @Override
                    public String merge(String a, String b) {
                        return a+"*"+b;
                    }
                })
                .print();
        System.out.println(env.getExecutionPlan());
        env.execute();
    }
}
