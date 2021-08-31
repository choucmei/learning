package com.chouc.flink.atguigu.example.business.topn.datastream;

import com.chouc.flink.atguigu.example.business.topn.ClickEvent;
import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.util.Collector;

public class FlinkStreamTopN {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setParallelism(1);
        StreamSourceUtils.getKafkaStream(executionEnvironment, "flink_learning")
                .flatMap(new FlatMapFunction<StreamSourceUtils.CustomRecord, ClickEvent>() {
                    @Override
                    public void flatMap(StreamSourceUtils.CustomRecord value, Collector<ClickEvent> out) throws Exception {
                        try {
                            String line = value.getValue();
                            String[] split = line.split(",");
                            out.collect(new ClickEvent(split[0], split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]), value.getTimestamp()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarksAdapter.Strategy<>(new BoundedOutOfOrdernessTimestampExtractor<ClickEvent>(Time.seconds(0)) {
            @Override
            public long extractTimestamp(ClickEvent element) {
                return element.getTimestamp();
            }
        }))
                .keyBy(ClickEvent::getAdGroup)
                .window(SlidingEventTimeWindows.of(Time.seconds(30), Time.seconds(30)))
                .aggregate(new AdAggFunc(), new AdAggWindowFunc())
                .keyBy(AdCountWithWindow::getWindowEnd)
                .process(new KeyedProcessFunction<Long, AdCountWithWindow, String>() {
                    ValueStateDescriptor<TopNByHeap> valueStateDescriptor;
                    ValueState<TopNByHeap> valueState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        valueStateDescriptor = new ValueStateDescriptor<TopNByHeap>("valueStateDescriptor", TopNByHeap.class);
                        valueState = getRuntimeContext().getState(valueStateDescriptor);
                    }

                    @Override
                    public void processElement(AdCountWithWindow value, Context ctx, Collector<String> out) throws Exception {
                        TopNByHeap topNByHeap;
                        if (valueState.value() == null) {
                            topNByHeap = new TopNByHeap(5);
                        } else {
                            topNByHeap = valueState.value();
                        }
                        topNByHeap.insert(value);
                        valueState.update(topNByHeap);
                        ctx.timerService().registerEventTimeTimer(value.windowEnd + 1);
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        AdCountWithWindow[] result = valueState.value().getResult();
                        for (AdCountWithWindow adCountWithWindow : result) {
                            out.collect(adCountWithWindow.toString());
                        }
                    }
                }).print();
        executionEnvironment.execute();
    }
}
