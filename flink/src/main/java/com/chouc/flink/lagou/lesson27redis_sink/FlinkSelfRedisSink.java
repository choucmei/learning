package com.chouc.flink.lagou.lesson27redis_sink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

/**
 * @author chouc
 * @version V1.0
 * @Title: SelfRedisSink
 * @Package com.chouc.flink.lagou.lession27redis_sink
 * @Description:
 * @date 2020/11/5
 */
public class FlinkSelfRedisSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<String> source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String k : value.split(",")) {
                    out.collect(Tuple2.of(k, 1));
                }
            }
        }).filter(t -> !t.f0.trim().equals("")).addSink(new SelfRedisSink());
        env.execute();
    }

    static class SelfRedisSink extends RichSinkFunction<Tuple2<String, Integer>> {
        private transient Jedis jedis;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            jedis = new Jedis("localhost", 6379);
        }

        @Override
        public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
            jedis.set(value.f0, String.valueOf(value.f1));
        }

        @Override
        public void close() throws Exception {
            super.close();
            jedis.close();
        }
    }
}
