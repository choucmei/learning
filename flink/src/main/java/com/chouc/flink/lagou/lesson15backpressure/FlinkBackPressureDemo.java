package com.chouc.flink.lagou.lesson15backpressure;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkBackPressureDemo
 * @Package com.chouc.flink.lagou.lession15backpressure
 * @Description:
 * @date 2020/9/25
 */
public class FlinkBackPressureDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> ds = env.addSource(new SocketTextStreamFunction("localhost",8888,"\n",3));
        ds.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String v:value.split(",")){
                    out.collect(Tuple2.of(v,1));
                }
            }
        }).keyBy(0).sum(1).map(new MapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                Thread.sleep(30 *1000);
                return value;
            }
        }).print();
        env.execute();
    }
}
