package com.chouc.flink.state.case_connect;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkConnect
 * @Package com.chouc.flink.state.case_connect
 * @Description:
 * @date 2021/3/31
 */
public class FlinkConnect {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Tuple2<String,String>> source = env.addSource(new SocketTextStreamFunction("localhost",9999,"\n",3))
                .map(new MapFunction<String, Tuple2<String,String>>() {
                    @Override
                    public Tuple2<String, String> map(String value) throws Exception {
                        String[] values = value.split(",");
                        return Tuple2.of(values[0],values[1]);
                    }
                });
        DataStream<Tuple2<String, String>> redis = env.addSource(new RedisSource("localhost",6379)).broadcast();
        source.connect(redis).flatMap(new RichCoFlatMapFunction<Tuple2<String,String>, Tuple2<String, String>, Tuple3<String,String,String>>() {

            Map<String,String> cache = new HashMap<>();

            @Override
            public void flatMap1(Tuple2<String,String> value, Collector<Tuple3<String,String,String>> out) throws Exception {
                out.collect(Tuple3.of(value.f0,value.f1,cache.getOrDefault(value.f1,"null")));
            }

            @Override
            public void flatMap2(Tuple2<String, String> value, Collector<Tuple3<String,String,String>> out) throws Exception {
                cache.put(value.f0,value.f1);
            }
        }).print();

        env.execute();
    }
}
