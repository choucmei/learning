package com.chouc.flink.lagou.lesson20efficient_distinct;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkDistinctState
 * @Package com.chouc.flink.lagou.lession20efficient_distinct
 * @Description:
 * @date 2020/10/10
 */
public class FlinkDistinctBitMapFilter {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> source = environment.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        source.flatMap(new FlatMapFunction<String, Long>() {

            @Override
            public void flatMap(String value, Collector<Long> out) throws Exception {
                for (String v : value.split(" ")) {
                    out.collect(Long.valueOf(Tuple2.of(v, 1).hashCode()));
                }
            }
        }).timeWindowAll(Time.seconds(5)).aggregate(new BitMapDistinct()).printToErr();
        environment.execute();
    }
}

class BitMapDistinct implements AggregateFunction<Long, Roaring64NavigableMap, Long> {

    @Override
    public Roaring64NavigableMap createAccumulator() {
        return new Roaring64NavigableMap();
    }

    @Override
    public Roaring64NavigableMap add(Long value, Roaring64NavigableMap accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public Long getResult(Roaring64NavigableMap accumulator) {
        return accumulator.getLongCardinality();
    }

    @Override
    public Roaring64NavigableMap merge(Roaring64NavigableMap a, Roaring64NavigableMap b) {
        return null;
    }
}