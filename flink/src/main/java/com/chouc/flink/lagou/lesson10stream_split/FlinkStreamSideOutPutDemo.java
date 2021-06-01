package com.chouc.flink.lagou.lesson10stream_split;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkStreamSideOutPutDemo
 * @Package com.chouc.flink.lagou.lession10stream_split
 * @Description:
 * @date 2020/9/20
 */
public class FlinkStreamSideOutPutDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        List<Tuple3<Integer, Integer, Integer>> list = new ArrayList<>();
        list.add(Tuple3.of(1, 1, 11));
        list.add(Tuple3.of(1, 2, 12));
        list.add(Tuple3.of(1, 3, 13));
        list.add(Tuple3.of(1, 4, 14));
        list.add(Tuple3.of(1, 5, 15));
        list.add(Tuple3.of(1, 6, 16));
        list.add(Tuple3.of(1, 7, 17));
        list.add(Tuple3.of(0, 1, 21));
        list.add(Tuple3.of(0, 2, 22));
        list.add(Tuple3.of(0, 3, 23));
        list.add(Tuple3.of(0, 4, 24));
        list.add(Tuple3.of(0, 5, 25));
        list.add(Tuple3.of(0, 6, 26));
        list.add(Tuple3.of(0, 7, 27));
        DataStream<Tuple3<Integer, Integer, Integer>> ds = env.fromCollection(list);
        OutputTag<Tuple3<Integer, Integer, Integer>> zeroDs = new OutputTag<Tuple3<Integer, Integer, Integer>>("zero") {
        };
        OutputTag<Tuple3<Integer, Integer, Integer>> oneDs = new OutputTag<Tuple3<Integer, Integer, Integer>>("one") {
        };
        SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> out = ds.process(new ProcessFunction<Tuple3<Integer, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
            @Override
            public void processElement(Tuple3<Integer, Integer, Integer> value, Context ctx, Collector<Tuple3<Integer, Integer, Integer>> out) throws Exception {
                if (value.f0 == 0) {
                    ctx.output(zeroDs, value);
                } else if (value.f0 == 1) {
                    ctx.output(oneDs, value);
                }
            }
        });
        out.getSideOutput(zeroDs).printToErr();
        out.getSideOutput(zeroDs).print();
        env.execute();
    }
}
