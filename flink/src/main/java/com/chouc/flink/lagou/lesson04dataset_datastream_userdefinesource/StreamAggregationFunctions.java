package com.chouc.flink.lagou.lesson04dataset_datastream_userdefinesource;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: StreamAggregationFunctions
 * @Package com.chouc.flink.lagou.lession04dataset_datastream
 * @Description:
 * @date 2020/8/23
 */
public class StreamAggregationFunctions {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        List<Tuple3<Integer, Integer, Integer>> collect = new ArrayList<>();
        collect.add(Tuple3.of(1, 1, 0));
        collect.add(Tuple3.of(1, 2, 1));
        collect.add(Tuple3.of(1, 3, 2));
        collect.add(Tuple3.of(0, 1, 3));
        collect.add(Tuple3.of(0, 2, 4));
        collect.add(Tuple3.of(0, 3, 6));
        collect.add(Tuple3.of(0, 4, 19));
        DataStreamSource<Tuple3<Integer, Integer, Integer>> streamSource = env.fromCollection(collect);
        KeyedStream<Tuple3<Integer, Integer, Integer>, Integer> keyedStream = streamSource.keyBy(new KeySelector<Tuple3<Integer, Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple3<Integer, Integer, Integer> in) throws Exception {
                return in.f0;
            }
        });

//        keyedStream.maxBy(2).print();

//        keyedStream.max(2).print();


        keyedStream.reduce(new ReduceFunction<Tuple3<Integer, Integer, Integer>>() {
            @Override
            public Tuple3<Integer, Integer, Integer> reduce(Tuple3<Integer, Integer, Integer> first, Tuple3<Integer, Integer, Integer> second) throws Exception {
                return Tuple3.of(first.f0 + second.f0, first.f1 + second.f1, first.f2 + second.f2);
            }
        }).print();


        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
