package com.chouc.flink.lagou.lesson28topn_demo;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkTopNDemo
 * @Package com.chouc.flink.lagou.lession28topn_demo
 * @Description:
 * @date 2020/11/5
 */
public class FlinkTopNDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        env.enableCheckpointing(60 * 1000, CheckpointingMode.EXACTLY_ONCE);
//        env.getCheckpointConfig().setCheckpointTimeout(30 * 1000);
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<String>("flink_learning", new SimpleStringSchema(), properties);
        consumer.setStartFromLatest();
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy
                .<String>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SerializableTimestampAssigner<String>() {
                    Long last = 0L;

                    @Override
                    public long extractTimestamp(String element, long recordTimestamp) {
                        try {
                            String[] values = element.split(",");
                            last = Long.parseLong(values[5]) * 1000;
                            System.out.println("timestamp:"+last);
                            return last;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return last;
                        }
                    }
                }));
        DataStreamSource<String> source = env.addSource(consumer);
        SingleOutputStreamOperator<OrderDetail> order = source.map(new MapFunction<String, OrderDetail>() {
            @Override
            public OrderDetail map(String value) throws Exception {
                String[] values = value.split(",");
                try {
                    OrderDetail orderDetail = new OrderDetail(values[0], values[1], values[2], Double.parseDouble(values[3]), values[4]);
                    return orderDetail;
                } catch (Exception e) {
                    return null;
                }
            }
        });
        DataStream<Tuple2<String, Double>> reduce = order.filter(v -> v != null).keyBy(new KeySelector<OrderDetail, String>() {
            @Override
            public String getKey(OrderDetail value) throws Exception {
                return value.getUserId();
            }
        }).window(SlidingEventTimeWindows.of(Time.seconds(600), Time.seconds(20)))
                .aggregate(new AggregateFunction<OrderDetail, Tuple2<String, Double>, Tuple2<String, Double>>() {

                    @Override
                    public Tuple2<String, Double> createAccumulator() {
                        return null;
                    }

                    @Override
                    public Tuple2<String, Double> add(OrderDetail value, Tuple2<String, Double> accumulator) {
                        if (accumulator == null) {
                            return Tuple2.of(value.getUserId(), value.getPrice());
                        } else {
                            return Tuple2.of(value.getUserId(), value.getPrice() + accumulator.f1);
                        }
                    }

                    @Override
                    public Tuple2<String, Double> getResult(Tuple2<String, Double> accumulator) {
                        return accumulator;
                    }

                    @Override
                    public Tuple2<String, Double> merge(Tuple2<String, Double> a, Tuple2<String, Double> b) {
                        if (a == null) {
                            return b;
                        }
                        if (b == null) {
                            return a;
                        }
                        if (a != null && b != null) {
                            return Tuple2.of(a.f0, a.f1 + b.f1);
                        }
                        return null;
                    }
                });
//        reduce.print();
        reduce.windowAll(TumblingEventTimeWindows.of(Time.seconds(20))).process(new ProcessAllWindowFunction<Tuple2<String, Double>, Tuple2<String, Double>, TimeWindow>() {

            @Override
            public void process(Context context, Iterable<Tuple2<String, Double>> elements, Collector<Tuple2<String, Double>> out) throws Exception {

                TreeMap<Double, String> treeMap = new TreeMap<Double, String>(new Comparator<Double>() {
                    @Override
                    public int compare(Double x, Double y) {
                        return (x < y) ? -1 : 1;
                    }
                });
                Iterator<Tuple2<String, Double>> iterator = elements.iterator();
                while (iterator.hasNext()) {
                    Tuple2<String, Double> t = iterator.next();
                    treeMap.put(t.f1, t.f0);
                    if (treeMap.size() > 10) {
                        treeMap.pollLastEntry();
                    }
                }

                for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
                    out.collect(Tuple2.of(entry.getValue(), entry.getKey()));
                }
            }
        }).print();
        env.execute();
    }
}
