package com.chouc.flink.lagou.lesson27redis_sink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkInnerRedisSink
 * @Package com.chouc.flink.lagou.lession27redis_sink
 * @Description:
 * @date 2020/11/5
 */
public class FlinkInnerRedisSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<String> source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        SingleOutputStreamOperator<Tuple2<String,Integer>> out = source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String k : value.split(",")) {
                    out.collect(Tuple2.of(k, 1));
                }
            }
        }).filter(t -> !t.f0.trim().equals(""));

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();
        out.addSink(new RedisSink<>(conf, new RedisMapper<Tuple2<String, Integer>>() {
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.SET);
            }

            @Override
            public String getKeyFromData(Tuple2<String, Integer> data) {
                return data.f0;
            }

            @Override
            public String getValueFromData(Tuple2<String, Integer> data) {
                return String.valueOf(data.f1);
            }
        }));
        env.execute();
    }
}
