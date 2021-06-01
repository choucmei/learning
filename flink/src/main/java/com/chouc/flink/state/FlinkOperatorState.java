package com.chouc.flink.state;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkOperatorState
 * @Package com.chouc.flink.state
 * @Description:
 * @date 2021/3/1
 */
public class FlinkOperatorState {
    public static void main(String[] args) throws Exception {

        class UDF implements CheckpointedFunction, FlatMapFunction<String, Tuple2<String, Integer>> {


            ListState<Integer> listState = null;

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String str : value.split(" ")) {
                    listState.add(1);
                    int sum = 0;
                    for (Integer i : listState.get()) {
                        sum += i;
                    }
                    out.collect(Tuple2.of(str, sum));
                }
            }


            @Override
            public void snapshotState(FunctionSnapshotContext context) throws Exception {

            }

            @Override
            public void initializeState(FunctionInitializationContext context) throws Exception {
                listState = context.getOperatorStateStore().getListState(new ListStateDescriptor<>("state", Types.INT));
            }
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3))
                .flatMap(new UDF()).print();
        env.execute();

    }


}
