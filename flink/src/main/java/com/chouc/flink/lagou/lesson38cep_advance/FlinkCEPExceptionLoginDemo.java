package com.chouc.flink.lagou.lesson38cep_advance;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
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
 * @Title: FlinkCEPExceptionLoginDemo
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class FlinkCEPExceptionLoginDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<LogInEvent> datasoure = env.fromElements(new LogInEvent(1L, "fail", 1597905234000L),
                new LogInEvent(1L, "success", 1597905235000L),
                new LogInEvent(2L, "fail", 1597905236000L),
                new LogInEvent(2L, "fail", 1597905237000L),
                new LogInEvent(2L, "fail", 1597905238000L),
                new LogInEvent(3L, "fail", 1597905239000L),
                new LogInEvent(3L, "success", 1597905240000L));
        KeyedStream<LogInEvent,Long> ds =  datasoure.assignTimestampsAndWatermarks(WatermarkStrategy.<LogInEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((e,ts)-> e.getTimestamp()))
            .keyBy(v -> v.getId());
        Pattern<LogInEvent,LogInEvent> exceptionLogin =Pattern.<LogInEvent>begin("start").where(new IterativeCondition<LogInEvent>() {
            @Override
            public boolean filter(LogInEvent value, Context<LogInEvent> ctx) throws Exception {
                return value.getIsSuccess().equals("fail");
            }

        }).timesOrMore(2).consecutive().within(Time.seconds(5));
        PatternStream<LogInEvent> patternStream = CEP.pattern(ds,exceptionLogin);
        patternStream.process(new PatternProcessFunction<LogInEvent, String>() {
            @Override
            public void processMatch(Map<String, List<LogInEvent>> match, Context ctx, Collector<String> out) throws Exception {
                List<LogInEvent> start = match.get("start");
                List<LogInEvent> next = match.get("next");
                System.err.println("start:" + start + ",next:" + next);
                out.collect(String.valueOf(start.get(0).getId()));
            }
        }).print();
        env.execute();
    }
}
