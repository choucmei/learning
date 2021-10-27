package com.chouc.flink.atguigu.example.business.count;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.sql.Timestamp;
import java.time.Duration;

public class Count {
    private static final Long MAX_COUNT = 50L;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setParallelism(1);
        OutputTag<String> outputTag = new OutputTag<String>("over size") {
        };
        SingleOutputStreamOperator<Order> filterOrder = executionEnvironment.readTextFile("C:\\Users\\chouc\\Desktop\\ym\\ym.csv")
                .flatMap(new FlatMapFunction<String, Order>() {
                    @Override
                    public void flatMap(String value, Collector<Order> out) throws Exception {
                        String[] split = value.split(",");
                        out.collect(new Order(split[0].trim(), split[1].trim(), split[2].trim(), split[3].trim(), split[4].trim(), Double.parseDouble(split[5]), Long.parseLong(split[6])));
                    }
                }).assignTimestampsAndWatermarks(WatermarkStrategy.<Order>forBoundedOutOfOrderness(Duration.ofSeconds(0)).withTimestampAssigner(new SerializableTimestampAssigner<Order>() {
                    @Override
                    public long extractTimestamp(Order element, long recordTimestamp) {
                        return element.getTimes();
                    }
                })).keyBy(new KeySelector<Order, String>() {
                    @Override
                    public String getKey(Order value) throws Exception {
                        return value.name + "-" + value.country;
                    }
                }).process(new KeyedProcessFunction<String, Order, Order>() {
                    ValueState<Long> countState = null;
                    ValueState<Boolean> booleanState = null;
                    ValueState<Long> stateCleanState = null;


                    @Override
                    public void open(Configuration parameters) throws Exception {
                        countState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("hour-count-state", Long.class));
                        booleanState = getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("black-state", Boolean.class));
                        stateCleanState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("clean-state", Long.class));
                    }

                    @Override
                    public void processElement(Order value, Context ctx, Collector<Order> out) throws Exception {
                        if (stateCleanState.value() == null) {
                            long currentWatermarkTime = ctx.timerService().currentProcessingTime();
                            long fireTime = (currentWatermarkTime / (1000 * 60 * 60) + 1) * (1000 * 60 * 60);
                            ctx.timerService().registerProcessingTimeTimer(fireTime);
                            stateCleanState.update(fireTime);
                        }
                        Long count = countState.value() == null ? 0L : countState.value();
                        if (MAX_COUNT <= count) {
                            boolean black = booleanState.value() == null ? false : booleanState.value();
                            if (!black) {
                                booleanState.update(true);
                                ctx.output(outputTag, value.name + "-" + value.country);
                            }
                        } else {
                            countState.update(count + 1l);
                            out.collect(value);
                        }
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Order> out) throws Exception {
                        Long cleanTime = stateCleanState.value() == null ? 0L : stateCleanState.value();
                        System.out.println(timestamp + " - " + cleanTime);
                        if (timestamp == cleanTime) {
                            stateCleanState.clear();
                            countState.clear();
                            booleanState.clear();
                        } else {
                            // pass
                        }
                    }
                });

        filterOrder
                .keyBy(new KeySelector<Order, String>() {

                    @Override
                    public String getKey(Order value) throws Exception {
                        return value.name + "-" + value.country;
                    }
                }).window(TumblingEventTimeWindows.of(Time.minutes(60)))
                .process(new ProcessWindowFunction<Order, String, String, TimeWindow>() {
                    ValueState<Long> valueState = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        valueState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("window-count", Long.class));
                    }

                    @Override
                    public void process(String stringStringTuple2, Context context, Iterable<Order> elements, Collector<String> out) throws Exception {
                        Long value = valueState.value() == null ? 0L : valueState.value();
                        String start = new Timestamp(context.window().getStart()).toString();
                        String end = new Timestamp(context.window().getEnd()).toString();
                        for (Order element : elements) {
//                            System.out.println(Thread.currentThread().getName() + " " + uid);
                            value += 1l;
                        }
                        if (value >= MAX_COUNT) {
                            System.out.println("@@@@@@@@@@@@@@@@@@ " + start + " " + end + " :" + stringStringTuple2 + " :" + value);
                        }
                        out.collect(start + " " + end + " :" + stringStringTuple2 + " :" + value);
                        valueState.update(value);
                    }
                }).print();

        filterOrder.getSideOutput(outputTag).print(" black ");

        executionEnvironment.execute();
    }
}
