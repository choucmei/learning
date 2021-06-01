package com.chouc.flink.lagou.lesson38cep_advance;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkCEPActiveUserDemo
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class FlinkCEPActiveUserDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<TransactionEvent> source = env.fromElements(
                new TransactionEvent("100XX", 0.0D, 1597905234000L),
                new TransactionEvent("100XX", 100.0D, 1597905235000L),
                new TransactionEvent("100XX", 200.0D, 1597905236000L),
                new TransactionEvent("100XX", 300.0D, 1597905237000L),
                new TransactionEvent("100XX", 400.0D, 1597905238000L),
                new TransactionEvent("100XX", 500.0D, 1597905239000L),
                new TransactionEvent("101XX", 0.0D, 1597905240000L),
                new TransactionEvent("101XX", 100.0D, 1597905241000L)
        );
        KeyedStream inputStream = source.assignTimestampsAndWatermarks(WatermarkStrategy.<TransactionEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((e, ts) -> e.getTimestamp()))
            .keyBy(v -> v.getAccount());
        Pattern<TransactionEvent,TransactionEvent> activeUser = Pattern.<TransactionEvent>begin("start").where(new SimpleCondition<TransactionEvent>() {
            @Override
            public boolean filter(TransactionEvent value) throws Exception {
                return value.getPrice() > 0;
            }
        }).timesOrMore(5).within(Time.days(1));
        PatternStream<TransactionEvent> patternStream = CEP.pattern(inputStream,activeUser);
        patternStream.process(new PatternProcessFunction<TransactionEvent,String>(){

            @Override
            public void processMatch(Map<String, List<TransactionEvent>> match, Context ctx, Collector<String> out) throws Exception {
                System.out.println("match:"+match.get("start"));
                out.collect(match.get("start").get(0).getAccount());
            }
        }).print();
        env.execute();
    }
}
