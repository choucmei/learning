package com.chouc.flink.state;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
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
public class FlinkKeyValueState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3))
                .flatMap(new RichFlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        int pre = 0;
                        for (String str : value.split(" ")) {
                            out.collect(Tuple2.of(str, 1));
                        }
                    }
                })
                .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .flatMap(new RichFlatMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {

                    ValueStateDescriptor<Integer> vsd = null;
                    ValueState<Integer> valueState = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        vsd = new ValueStateDescriptor<Integer>("count", Integer.class);
                        valueState = getRuntimeContext().getState(vsd);
                    }

                    @Override
                    public void flatMap(Tuple2<String, Integer> value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        int pre = 0;
                        if (valueState.value() != null) {
                            pre = valueState.value();
                        }
                        valueState.update(pre + value.f1);
                        out.collect(Tuple2.of(value.f0, valueState.value() + value.f1));
                    }

                    @Override
                    public void close() throws Exception {
                        super.close();
                    }
                }).print();
        env.execute();

    }
}
