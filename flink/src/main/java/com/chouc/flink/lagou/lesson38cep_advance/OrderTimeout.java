package com.chouc.flink.lagou.lesson38cep_advance;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chouc
 * @version V1.0
 * @Title: OrderTimeout
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class OrderTimeout {

    public static class DataSource implements Iterator<PayEvent>, Serializable {
        private final AtomicInteger atomicInteger = new AtomicInteger(0);
        private final List<PayEvent> PayEventList = Arrays.asList(
                new PayEvent(1L, "create",System.currentTimeMillis()),
                new PayEvent(1L, "create",System.currentTimeMillis()),
                new PayEvent(2L, "pay",System.currentTimeMillis())
        );

        @Override
        public boolean hasNext() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public PayEvent next() {
            return PayEventList.get(atomicInteger.getAndIncrement() % 3);
        }
    }
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<PayEvent> loginEventStream = env.fromElements(
                new PayEvent(1L, "create", 1597905234000L),
                new PayEvent(1L, "pay", 1597905235000L),
                new PayEvent(2L, "create", 1597905236000L),
                new PayEvent(2L, "pay", 1597905237000L),
                new PayEvent(3L, "create", 1597905239000L),
                new PayEvent(4L, "create", 1597905949000L)
        );

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
                .within(Time.seconds(1));

        PatternStream<PayEvent> patternStream = CEP.pattern(
                loginEventStream.keyBy(PayEvent::getUserId),
                loginFailPattern);

        OutputTag<String> orderTiemoutOutput = new OutputTag<String>("orderTimeout") {
        };

        SingleOutputStreamOperator<String> complexResult = patternStream.select(
                orderTiemoutOutput,
                (PatternTimeoutFunction<PayEvent, String>) (map, l) -> {
                    System.out.println("hhhhhhhh"+l);
                    return String.valueOf(map.get("begin").get(0).getUserId());
                },
                (PatternSelectFunction<PayEvent, String>) map -> {
                    System.out.println("hhhhhhhh");
                    return String.valueOf(map.get("begin").get(0).getUserId());
                }

        );

        DataStream<String> timeoutResult = complexResult.getSideOutput(orderTiemoutOutput);

        timeoutResult.print();

        env.execute();
    }
}
