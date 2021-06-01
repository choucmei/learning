package com.chouc.flink.lagou.lesson20efficient_distinct;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
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
public class FlinkDistinctBloomFilter {
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
        }).keyBy(0).process(new BloomFilterDistinctFunction()).printToErr();
        environment.execute();
    }
}

class BloomFilterDistinctFunction extends KeyedProcessFunction<Tuple, Tuple2<String, Integer>, Tuple2<String, Integer>> {
    ValueState<BloomFilter> bloomState;
    ValueState<Integer> countState;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ValueStateDescriptor<BloomFilter> bloomVSD = new ValueStateDescriptor<BloomFilter>("bloom", BloomFilter.class);
        ValueStateDescriptor<Integer> VSD = new ValueStateDescriptor<Integer>("value",Integer.class);
        bloomState = getRuntimeContext().getState(bloomVSD);
        countState = getRuntimeContext().getState(VSD);
    }

    @Override
    public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
        BloomFilter bloomFilter = bloomState.value();
        Integer skuCount = countState.value();
        if (bloomFilter == null) {
            bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), 10000000);
        }
        if (skuCount == null) {
            skuCount = 0;
        }
        if (!bloomFilter.mightContain(value.f0)) {
            bloomFilter.put(value.f0);
            skuCount = skuCount + 1;
        }
        bloomState.update(bloomFilter);
        countState.update(skuCount);
        out.collect(Tuple2.of(value.f0, countState.value()));
    }
}