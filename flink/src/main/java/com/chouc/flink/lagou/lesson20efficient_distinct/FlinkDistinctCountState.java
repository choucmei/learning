package com.chouc.flink.lagou.lesson20efficient_distinct;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkDistinctState
 * @Package com.chouc.flink.lagou.lession20efficient_distinct
 * @Description:
 * @date 2020/10/10
 */
public class FlinkDistinctCountState {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> source = environment.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String v : value.split(" ")) {
                    out.collect(Tuple2.of(v, 1));
                }
            }
        }).keyBy(0).process(new MapStateDistinctFunction()).printToErr();
        environment.execute();
    }
}

class MapStateDistinctFunction extends KeyedProcessFunction<Tuple, Tuple2<String, Integer>, Tuple2<String, Integer>> {

    private transient ValueState<Integer> counts;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //我们设置 ValueState 的 TTL 的生命周期为24小时，到期自动清除状态
        StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.hours(24))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();
        ValueStateDescriptor<Integer> vsd = new ValueStateDescriptor("count", Integer.class);
        vsd.enableTimeToLive(stateTtlConfig);
        counts = getRuntimeContext().getState(vsd);
    }

    @Override
    public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
        String f0 = value.f0;
        //如果不存在则新增
        if (counts.value() == null) {
            counts.update(1);
        } else {
            //如果存在则加1
            counts.update(counts.value() + 1);
        }
        out.collect(Tuple2.of(f0, counts.value()));
    }
}