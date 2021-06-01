package com.chouc.flink.lagou.lesson38cep_advance;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkCEPUnpayOrderDemo
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class FlinkCEPUnpayOrderDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<PayEvent> source = env.fromElements(
                new PayEvent(1L, "create", 1597905234000L),
                new PayEvent(1L, "pay", 1597905235000L),
                new PayEvent(2L, "create", 1597905236000L),
                new PayEvent(2L, "pay", 1597905237000L),
                new PayEvent(3L, "create", 1597905239000L),
                new PayEvent(4L, "create", 1597905949000L)

        ).assignTimestampsAndWatermarks(WatermarkStrategy.<PayEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
            .withTimestampAssigner((e,ts)->e.getTimestamp()))
        .keyBy(v -> v.getUserId());
        Pattern<PayEvent, PayEvent> loginFailPattern = Pattern.<PayEvent>
                begin("begin")
                .where(new IterativeCondition<PayEvent>() {
                    @Override
                    public boolean filter(PayEvent loginEvent, Context context) throws Exception {
                        return loginEvent.getPay().equals("create");
                    }
                })
                .next("next")
                .where(new IterativeCondition<PayEvent>() {
                    @Override
                    public boolean filter(PayEvent loginEvent, Context context) throws Exception {
                        return loginEvent.getPay().equals("pay");
                    }
                })
                .within(Time.seconds(60*10));
        OutputTag<PayEvent> orderTimeoutOutput = new OutputTag<PayEvent>("orderTimeout") {};
        PatternStream<PayEvent> patternStream = CEP.pattern(source,loginFailPattern);
        SingleOutputStreamOperator<PayEvent> result = patternStream.select(orderTimeoutOutput, new PatternTimeoutFunction<PayEvent, PayEvent>() {
            @Override
            public PayEvent timeout(Map<String, List<PayEvent>> pattern, long timeoutTimestamp) throws Exception {
                System.out.println("timeout begin:"+pattern.get("begin")+" timeoutTimestamp:"+timeoutTimestamp);
                return pattern.get("begin").get(0);
            }
        }, new PatternSelectFunction<PayEvent, PayEvent>() {
            @Override
            public PayEvent select(Map<String, List<PayEvent>> pattern) throws Exception {
                System.out.println("begin:"+pattern.get("begin"));
                System.out.println("next:"+pattern.get("next"));
                return pattern.get("next").get(0);
            }
        });
        DataStream<PayEvent> sideOutput = result.getSideOutput(orderTimeoutOutput);
        sideOutput.print();
        env.execute();
    }
}
