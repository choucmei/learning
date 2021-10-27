package com.chouc.flink.atguigu.example.business.uv.datastream;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class FlinkDataStreamUv {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        OutputTag<Tuple2<String, Long>> late = new OutputTag<Tuple2<String, Long>>("late") {
        };
        executionEnvironment.readTextFile("C:\\Users\\chouc\\Desktop\\ym\\ym.csv")
                .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        String[] split = value.split(",");
                        out.collect(Tuple2.of(split[0], Long.parseLong(split[6])));
                    }
                }).assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(0)).withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
            @Override
            public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                return element.f1;
            }
        })).windowAll(TumblingEventTimeWindows.of(Time.minutes(60)))
                .trigger(new Trigger<Tuple2<String, Long>, TimeWindow>() {
                    @Override
                    public TriggerResult onElement(Tuple2<String, Long> element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.FIRE_AND_PURGE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return null;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {

                    }
                })
                .process(new ProcessAllWindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, TimeWindow>() {
                    Jedis jedis;
                    BloomFilter bloomFilter = new BloomFilter(1 << 29);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String uvCountKey = "uvcount";

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        jedis = new Jedis("s1", 6379);
                    }

                    @Override
                    public void process(Context context, Iterable<Tuple2<String, Long>> elements, Collector<Tuple2<String, Long>> out) throws Exception {
                        String dataString = simpleDateFormat.format(new Date(context.window().getEnd()));
                        Long uvCount = 0L;
                        if (jedis.hget(uvCountKey, dataString) != null) {
                            uvCount = Long.parseLong(jedis.hget(uvCountKey, dataString));
                        }
                        Tuple2<String, Long> next = elements.iterator().next();
                        Long hash = bloomFilter.hash(next.f0, 5);
                        if (!jedis.getbit(dataString, hash)) {
                            jedis.setbit(dataString, hash, true);
                            jedis.hset(uvCountKey, dataString, String.valueOf(uvCount + 1));
                        }
                    }
                }).print();
        executionEnvironment.execute();
    }
}
