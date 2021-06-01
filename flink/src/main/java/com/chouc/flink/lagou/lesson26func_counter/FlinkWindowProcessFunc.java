package com.chouc.flink.lagou.lesson26func_counter;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Iterator;
import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkWindowProcessFunc
 * @Package com.chouc.flink.lagou.lession26func
 * @Description:
 * @date 2020/11/3
 */
public class FlinkWindowProcessFunc {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer("flink_learning", new SimpleStringSchema(), properties);
        flinkKafkaConsumer.setStartFromLatest();
        DataStreamSource<String> source = env.addSource(flinkKafkaConsumer);

        source.map(new RichMapFunction<String, Tuple2<String,Long>>() {
            @Override
            public Tuple2<String,Long> map(String value) throws Exception {
                String[] values = value.split(",");
                return Tuple2.of(values[0],Long.parseLong(values[1]));
            }
        }).keyBy(new KeySelector<Tuple2<String, Long>, String>() {

            @Override
            public String getKey(Tuple2<String, Long> value) throws Exception {
                return value.f0;
            }
        })
            .timeWindow(Time.seconds(20))
            .process(new ProcessWindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, String, TimeWindow>() {
                @Override
                public void process(String s,Context context, Iterable<Tuple2<String, Long>> elements, Collector<Tuple2<String, Long>> out) throws Exception {
                    System.out.println("context.currentWatermark():"+context.currentWatermark());
                    Iterator<Tuple2<String, Long>> it = elements.iterator();
                    Long min = Long.MAX_VALUE;
                    while (it.hasNext()) {
                        Tuple2<String, Long> value = it.next();
                        System.out.println("key:" + s + " value:" + value);
                        if (value.f1 < min) {
                            min = value.f1;
                        }
                    }
                    out.collect(Tuple2.of(s, min));
                }
            }).print();
        env.execute();
    }
}
