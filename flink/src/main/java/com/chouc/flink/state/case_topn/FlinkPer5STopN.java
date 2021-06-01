package com.chouc.flink.state.case_topn;


import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkPer5STopN
 * @Package com.chouc.flink.state
 * @Description:
 * @date 2021/3/9
 */
public class FlinkPer5STopN {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 2));
        source.map(new MapFunction<String, RequestEntity>() {
            @Override
            public RequestEntity map(String value) throws Exception {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String[] values = value.split(",");
                return new RequestEntity(values[0], values[1], values[2], values[3], simpleDateFormat.parse(values[4]));
            }
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<RequestEntity>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((element, recordTimestamp) -> element.getDate().getTime()))
                .keyBy(RequestEntity::getUrl)
                .window(SlidingEventTimeWindows.of(Time.seconds(5), Time.seconds(5)))
                .aggregate(new AggregateFunction<RequestEntity, Long, Long>() {
                    @Override
                    public Long createAccumulator() {
                        return 0L;
                    }

                    @Override
                    public Long add(RequestEntity value, Long accumulator) {
                        System.out.println("add accumulator:"+accumulator);
                        return accumulator + 1;
                    }

                    @Override
                    public Long getResult(Long accumulator) {
                        System.out.println("getResult accumulator:"+accumulator);
                        return accumulator;
                    }

                    @Override
                    public Long merge(Long a, Long b) {
                        System.out.println("merge a:"+a+" b:"+b);
                        return a + b;
                    }
                }, new WindowFunction<Long, URLCount, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow window, Iterable<Long> input, Collector<URLCount> out) throws Exception {
                        Long count = input.iterator().next();
                        System.out.println("window func url:" + s + " count:" + count);
                        out.collect(new URLCount(s, count, window.getEnd()));
                    }
                }).keyBy(URLCount::getWindowEnd)
                .process(new KeyedProcessFunction<Long, URLCount, String>() {
                    ListStateDescriptor<Tuple2<String, Long>> lsd = null;
                    ListState<Tuple2<String, Long>> listState = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        lsd = new ListStateDescriptor<Tuple2<String, Long>>("ls", TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {
                        }));
                        listState = getRuntimeContext().getListState(lsd);
                    }

                    @Override
                    public void processElement(URLCount value, Context ctx, Collector<String> out) throws Exception {
                        listState.add(Tuple2.of(value.getUrl(), value.getCount()));
                        ctx.timerService().registerEventTimeTimer(value.getWindowEnd() + 1);
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        Iterator<Tuple2<String, Long>> it = listState.get().iterator();
                        List<Tuple2<String, Long>> list = IteratorUtils.toList(it);
                        list.sort(new Comparator<Tuple2<String, Long>>() {
                            @Override
                            public int compare(Tuple2<String, Long> o1, Tuple2<String, Long> o2) {
                                return o1.f1 > o2.f1 ? 1 : 0;
                            }
                        });
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        StringBuilder stringBuilder = new StringBuilder(simpleDateFormat.format(new Date(timestamp))).append("\n");
                        int maxSize = 5;
                        int curSize = 1;
                        for (Tuple2<String, Long> t : list) {
                            if (curSize > maxSize) {
                                break;
                            }
                            stringBuilder.append(" url:").append(t.f0).append(" count:").append(t.f1).append(" num:").append(curSize++).append("\n");
                        }
                        listState.clear();
                        out.collect(stringBuilder.toString());
                    }
                }).print();

        env.execute();
    }
}
