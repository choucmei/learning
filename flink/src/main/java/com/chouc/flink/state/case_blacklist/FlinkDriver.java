package com.chouc.flink.state.case_blacklist;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkDriver
 * @Package com.chouc.flink.state.case_blacklist
 * @Description:
 * @date 2021/3/10
 */
public class FlinkDriver {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        OutputTag<String> outputTag = new OutputTag<String>("blackList") {
        };

        DataStreamSource<String> source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 1));
        SingleOutputStreamOperator<ClickStreamEntity> ds = source.map(new RichMapFunction<String, ClickStreamEntity>() {
            SimpleDateFormat simpleDateFormat;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            @Override
            public ClickStreamEntity map(String line) throws Exception {
                String[] values = line.split(",");
                Date date = simpleDateFormat.parse(values[2]);
                return new ClickStreamEntity(values[0], values[1], date);
            }
        }).assignTimestampsAndWatermarks(WatermarkStrategy.forGenerator(new WatermarkGeneratorSupplier<ClickStreamEntity>() {
            @Override
            public WatermarkGenerator<ClickStreamEntity> createWatermarkGenerator(Context context) {
                return new WatermarkGenerator<ClickStreamEntity>() {
                    /** The maximum timestamp encountered so far. */
                    private long maxTimestamp = 0;

                    /** The maximum out-of-orderness that this watermark generator assumes. */
                    private final long outOfOrdernessMillis = 0;


                    // ------------------------------------------------------------------------

                    @Override
                    public void onEvent(ClickStreamEntity event, long eventTimestamp, WatermarkOutput output) {
                        Long et = event.getDate().getTime();
                        System.out.println(" event time:"+et);
                        maxTimestamp = Math.max(maxTimestamp, et);
                    }

                    @Override
                    public void onPeriodicEmit(WatermarkOutput output) {
                        output.emitWatermark(new Watermark(maxTimestamp - outOfOrdernessMillis));
                    }
                };
            }
        })).keyBy(new KeySelector<ClickStreamEntity, String>() {
            @Override
            public String getKey(ClickStreamEntity value) throws Exception {
                return value.getUserId() + "-" + value.getAdId();
            }
        }).process(new KeyedProcessFunction<String, ClickStreamEntity, ClickStreamEntity>() {
            ValueState<Long> countValueState;
            ValueState<Long> tsValueState;
            ValueState<Boolean> inBlackList;

            Long preValue = 0L;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                countValueState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("countVsd", Long.class));
                tsValueState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("tsVsd", Long.class));
                inBlackList = getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("inBlackList", Boolean.class));
            }

            @Override
            public void processElement(ClickStreamEntity value, Context ctx, Collector<ClickStreamEntity> out) throws Exception {
                long wm = ctx.timerService().currentWatermark();
                long pt = ctx.timerService().currentProcessingTime();
                if (countValueState.value() != null) {
                    preValue = countValueState.value();
                } else {
                    preValue = 0L;
                    long fireTimes = (pt / (1000 * 60) + 1) * (1000 * 60);
                    System.out.println("wm:" + wm + " pt:" + pt + " fireTimes:" + fireTimes);
                    tsValueState.update(fireTimes);
                    ctx.timerService().registerProcessingTimeTimer(fireTimes);
                }
                Long curValue = preValue + 1;
                System.out.println("uid:" + value.getUserId() + " aid:" + value.getAdId() + "preValue:" + preValue+" curValue:"+curValue);
                if (curValue >= 5) {
                    if (inBlackList.value() == null || !inBlackList.value()) {
                        inBlackList.update(true);
                        ctx.output(outputTag, value.getUserId() + " " + value.getAdId() + " count:" + preValue);
                    }
                }
                countValueState.update(curValue);
                out.collect(value);
            }

            @Override
            public void onTimer(long timestamp, KeyedProcessFunction<String, ClickStreamEntity, ClickStreamEntity>.OnTimerContext ctx, Collector<ClickStreamEntity> out) throws Exception {
                if (timestamp == tsValueState.value()) {
                    countValueState.clear();
                    tsValueState.clear();
                    inBlackList.clear();
                }
            }
        });
        ds.print();
        ds.getSideOutput(outputTag).printToErr();
        System.out.println(env.getExecutionPlan());
        env.execute();


    }
}
