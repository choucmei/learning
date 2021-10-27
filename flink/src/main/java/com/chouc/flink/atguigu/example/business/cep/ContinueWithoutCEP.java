package com.chouc.flink.atguigu.example.business.cep;

import com.chouc.flink.atguigu.example.business.count.Order;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

public class ContinueWithoutCEP {
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

        OutputTag<String> payFailed = new OutputTag<String>("payfail") {
        };

        SingleOutputStreamOperator<String> process = orderStringKeyedStream.process(new KeyedProcessFunction<String, Order, String>() {
            ValueState<Long> createTime = null;
            MapState<String, Order> mapState = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                createTime = getRuntimeContext().getState(new ValueStateDescriptor<Long>("createTime", Long.class));
                mapState = getRuntimeContext().getMapState(new MapStateDescriptor<String, Order>("mapState", String.class, Order.class));
            }

            @Override
            public void processElement(Order value, Context ctx, Collector<String> out) throws Exception {
                if (value.getCountry().equals("国产")) {
                    Long times = value.getTimes() + 5 * 1000;
                    ctx.timerService().registerEventTimeTimer(times);
                    createTime.update(times);
                    mapState.put("first", value);
                } else {
                    if (createTime.value() != null) {
                        ctx.timerService().deleteEventTimeTimer(createTime.value());
                        mapState.put("second", value);
                        out.collect("start--- \n" + mapState.get("first").toString() + "  \n" + value.toString() + "\n ---end");
                    }
                }
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                Order first = mapState.get("first");
                ctx.output(payFailed, first.toString() + " " + first.getTimes() + 5000);
                createTime.clear();
                mapState.clear();
            }
        });
        process.print();
        process.getSideOutput(payFailed).print(" **** ");
        executionEnvironment.execute(" --------- ");
    }
}
