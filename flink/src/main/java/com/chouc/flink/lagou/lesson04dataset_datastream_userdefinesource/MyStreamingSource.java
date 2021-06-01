package com.chouc.flink.lagou.lesson04dataset_datastream_userdefinesource;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * @author chouc
 * @version V1.0
 * @Title: MyStreamingSource
 * @Package com.chouc.flink.lagou.lession04dataset_datastream
 * @Description:
 * @date 2020/8/20
 */
class UserDefineSource implements SourceFunction<OrderEntity> {
    boolean cancel = true;

    @Override
    public void run(SourceContext<OrderEntity> sourceContext) throws Exception {
        Random random = new Random();
        while (cancel) {
            sourceContext.collect(new OrderEntity("__id" + random.nextInt(), "__name" + random.nextInt()));
        }
    }

    @Override
    public void cancel() {
        cancel = false;
    }
}

class OrderEntity {
    public String id;
    public String name;

    public OrderEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

class StreamingDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<OrderEntity> stream = env.addSource(new UserDefineSource());
        DataStream<String> outstream = stream.map(new MapFunction<OrderEntity, String>() {
            @Override
            public String map(OrderEntity orderEntity) throws Exception {
                return orderEntity.getName();
            }
        });
        outstream.print();
        env.execute();
    }

}