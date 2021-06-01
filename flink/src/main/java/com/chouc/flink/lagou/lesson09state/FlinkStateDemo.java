package com.chouc.flink.lagou.lesson09state;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkStateDemo
 * @Package com.chouc.flink.lagou.lession09state
 * @Description:
 * @date 2020/9/18
 */
public class FlinkStateDemo {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<Tuple2<String,Integer>> ds = env.addSource(new SocketTextStreamFunction("localhost",8888,"\n",3))
                .flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        for (String v:value.split(",")){
                            System.out.println("inpu:"+v);
                            out.collect(Tuple2.of(v,1));
                        }
                    }
                });
        ds.keyBy(0).flatMap(new RichFlatMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(Tuple2<String, Integer> value, Collector<Tuple2<String, Integer>> out) throws Exception {
                Tuple2<String, Integer> currentSum ;
                if (sum.value() == null){
                    currentSum = Tuple2.of(value.f0,0);
                } else {
                    currentSum = sum.value();
                }
                currentSum = Tuple2.of(currentSum.f0,currentSum.f1+value.f1);
                sum.update(currentSum);
                if (currentSum.f1 == 10){
                    sum.clear();
                    out.collect(currentSum);
                }
            }

            private transient ValueState<Tuple2<String, Integer>> sum;

            @Override
            public void open(Configuration parameters) throws Exception {
                ValueStateDescriptor vsd = new ValueStateDescriptor("sum", TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}));
                StateTtlConfig stc = StateTtlConfig.newBuilder(Time.seconds(10))
                        .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                        .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                        .build();
                vsd.enableTimeToLive(stc);
                sum = getRuntimeContext().getState(vsd);
            }
        }).printToErr();
        env.execute();

    }
}
