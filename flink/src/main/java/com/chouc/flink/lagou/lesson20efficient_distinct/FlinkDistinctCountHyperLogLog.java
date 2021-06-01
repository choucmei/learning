package com.chouc.flink.lagou.lesson20efficient_distinct;

import net.agkn.hll.HLL;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkDistinctCountHyperLogLog
 * @Package com.chouc.flink.lagou.lession19dimemsion_join
 * @Description:
 * @date 2020/10/10
 */
public class FlinkDistinctCountHyperLogLog {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment();
        //设置为eventtime事件类型
//        environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //设置水印生成时间间隔100ms
//        environment.getConfig().setAutoWatermarkInterval(100);
        DataStreamSource<String> source = environment.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        source.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                for (String v : value.split(" ")) {
                    out.collect(Tuple2.of(v, 1L));
                }
            }
        }).keyBy(0).window(TumblingProcessingTimeWindows.of(Time.seconds(5))).aggregate(new HyperLogLogDistinct()).print();
        environment.execute();
    }
}

class HyperLogLogDistinct implements AggregateFunction<Tuple2<String, Long>, HLL, Long> {

    @Override
    public HLL createAccumulator() {
        return new HLL(14, 5);
    }

    @Override
    public HLL add(Tuple2<String, Long> value, HLL accumulator) {
        //value 为访问记录 <商品sku, 用户id>
        accumulator.addRaw(value.f1);
        return accumulator;
    }

    @Override
    public Long getResult(HLL accumulator) {
        long cardinality = accumulator.cardinality();
        return cardinality;
    }

    @Override
    public HLL merge(HLL a, HLL b) {
        a.union(b);
        return a;
    }

}
