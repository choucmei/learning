package com.chouc.flink.state;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: StateStreamDemo
 * @Package com.chouc.flink.state
 * @Description:
 * @date 7/16/20
 */
public class FlinkKeyAggregatingState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3))
                .flatMap(new RichFlatMapFunction<String, Tuple3<String, String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple3<String, String, Integer>> out) throws Exception {
                        String[] values = value.split(" ");
                        out.collect(Tuple3.of(values[0], values[1], Integer.parseInt(values[2])));
                    }
                })
                .keyBy(new KeySelector<Tuple3<String, String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple3<String, String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .flatMap(new RichFlatMapFunction<Tuple3<String, String, Integer>, Tuple2<String, Integer>>() {

                    AggregatingStateDescriptor<Tuple3<String, String, Integer>, Integer, Integer> vsd = null;
                    AggregatingState<Tuple3<String, String, Integer>, Integer> aggregatingState = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        vsd = new AggregatingStateDescriptor("list", new AggregateFunction<Tuple3<String, String, Integer>, Integer, Integer>() {
                            Integer total = 0;
                            @Override
                            public Integer createAccumulator() {
                                return total;
                            }

                            @Override
                            public Integer add(Tuple3<String, String, Integer> value, Integer accumulator) {
                                return accumulator+value.f2;
                            }

                            @Override
                            public Integer getResult(Integer accumulator) {
                                return accumulator;
                            }

                            @Override
                            public Integer merge(Integer a, Integer b) {
                                return a+b;
                            }
                        }, Integer.class);
                        aggregatingState = getRuntimeContext().getAggregatingState(vsd);
                    }

                    @Override
                    public void flatMap(Tuple3<String, String, Integer> value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        aggregatingState.add(value);
                        out.collect(Tuple2.of(value.f0,aggregatingState.get()));
                    }

                    @Override
                    public void close() throws Exception {
                        super.close();
                    }
                }).print();
        env.execute();

    }
}
