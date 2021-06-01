package com.chouc.flink.lagou.lesson04dataset_datastream_userdefinesource;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: StreamFunctions
 * @Package com.chouc.flink.lagou.lession04dataset_datastream
 * @Description:
 * @date 2020/8/23
 */
public class StreamFunctions {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<OrderEntity> stream = env.addSource(new UserDefineSource());

        DataStream<String> outstream01 = stream.map(new MapFunction<OrderEntity, String>() {
            @Override
            public String map(OrderEntity orderEntity) throws Exception {
                return orderEntity.getName();
            }
        });

        DataStream<OrderEntity> outstream02 = stream.flatMap(new FlatMapFunction<OrderEntity, OrderEntity>() {
            @Override
            public void flatMap(OrderEntity orderEntity, Collector<OrderEntity> collector) throws Exception {
                collector.collect(orderEntity);
                collector.collect(orderEntity);
            }
        });

        DataStream<OrderEntity> outstream03 = stream.filter(new FilterFunction<OrderEntity>() {
            @Override
            public boolean filter(OrderEntity orderEntity) throws Exception {
                return orderEntity.id.hashCode() % 2 == 0;
            }
        });

        DataStream<OrderEntity> outstream0301 = stream.filter(entity -> entity.id.hashCode() % 2 == 0);

        KeyedStream<OrderEntity, String> outstream04 = stream.keyBy(new KeySelector<OrderEntity, String>() {
            @Override
            public String getKey(OrderEntity orderEntity) throws Exception {
                return orderEntity.getId();
            }
        });

        // lambda
        KeyedStream<OrderEntity, String> outstream0401 = stream.keyBy(OrderEntity::getId);
        outstream03.print();
        env.execute();
    }

}
