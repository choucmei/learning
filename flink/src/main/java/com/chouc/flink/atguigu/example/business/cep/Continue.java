package com.chouc.flink.atguigu.example.business.cep;

import com.chouc.flink.atguigu.example.business.count.Order;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Continue {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setParallelism(1);
        OutputTag<String> outputTag = new OutputTag<String>("over size") {
        };
        KeyedStream<Order, String> orderStringKeyedStream = executionEnvironment.readTextFile("C:\\Users\\chouc\\Desktop\\ym\\ym.csv")
                .flatMap(new FlatMapFunction<String, Order>() {
                    @Override
                    public void flatMap(String value, Collector<Order> out) throws Exception {
                        String[] split = value.split(",");
                        Order order = new Order(split[0].trim(), split[1].trim(), split[2].trim(), split[3].trim(), split[4].trim(), Double.parseDouble(split[5]), Long.parseLong(split[6]));
                        out.collect(order);
                    }
                })
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Order>forBoundedOutOfOrderness(Duration.ofSeconds(0)).withTimestampAssigner(new SerializableTimestampAssigner<Order>() {
                    @Override
                    public long extractTimestamp(Order element, long recordTimestamp) {
                        return element.getTimes();
                    }
                })).keyBy(Order::getName);

        Pattern<Order, Order> within = Pattern.<Order>begin("first")
                .where(new IterativeCondition<Order>() {
                    @Override
                    public boolean filter(Order order, Context<Order> context) throws Exception {
                        return order.getCountry().equals("国产");
                    }
                }).followedByAny("secend")
                .where(new IterativeCondition<Order>() {
                    @Override
                    public boolean filter(Order order, Context<Order> context) throws Exception {
                        return !order.getCountry().equals("国产");
                    }
                }).within(Time.seconds(5));
        PatternStream<Order> pattern = CEP.pattern(orderStringKeyedStream, within);
        SingleOutputStreamOperator<String> select = pattern.select(outputTag, new PatternTimeoutFunction<Order, String>() {
            @Override
            public String timeout(Map<String, List<Order>> pattern, long timeoutTimestamp) throws Exception {
                for (Order first : pattern.get("first")) {
                }
                return pattern.get("first").get(0) + " " + timeoutTimestamp;
            }
        }, new PatternSelectFunction<Order, String>() {
            @Override
            public String select(Map<String, List<Order>> pattern) throws Exception {
                List<Order> first = pattern.get("first");
                List<Order> secend = pattern.get("secend");
                return "start--- \n" + first.get(0).toString() + "  \n" + secend.get(0).toString() + "\n ---end";
            }
        });
        select.getSideOutput(outputTag).print("-------");
        select.print();

        executionEnvironment.execute();

    }
}
