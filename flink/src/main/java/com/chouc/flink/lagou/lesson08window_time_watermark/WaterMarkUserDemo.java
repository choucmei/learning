package com.chouc.flink.lagou.lesson08window_time_watermark;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;

/**
 * @author chouc
 * @version V1.0
 * @Title: WaterMarkUserDemo
 * @Package com.chouc.flink.lagou.lession08window_time_watermark
 * @Description:
 * @date 2020/9/8
 */
public class WaterMarkUserDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(50);
        DataStream<Tuple2<String, Long>> ds = env.addSource(new SocketTextStreamFunction("localhost", 8888, "\n", 3))
                .map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String value) throws Exception {
                        try {
                            String[] values = value.split(",");
                            return Tuple2.of(values[0], Long.valueOf(values[1]));
                        } catch (Exception e) {
                            return Tuple2.of("null", 0L);
                        }
                    }
                }).setParallelism(1);
        ds.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {
            private Long currentTimeStamp = 0L;
            //设置允许乱序时间
            private Long maxOutOfOrderness = 0L;

            @Override
            public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                currentTimeStamp = Math.max(element.f1, currentTimeStamp);
                System.err.println(element.f0 + ",EventTime:" + element.f1 + ",watermark:" + (currentTimeStamp - maxOutOfOrderness));
                return element.f1;
            }

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return new Watermark(currentTimeStamp - maxOutOfOrderness);
            }
        }).keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .maxBy(1)
                .print();
        env.execute();
    }
}
