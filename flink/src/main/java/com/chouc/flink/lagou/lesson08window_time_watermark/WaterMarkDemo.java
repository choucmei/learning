package com.chouc.flink.lagou.lesson08window_time_watermark;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author chouc
 * @version V1.0
 * @Title: WaterMarkDemo
 * @Package com.chouc.flink.lagou.lession08window_time_watermark
 * @Description:
 * @date 2020/9/6
 */
public class WaterMarkDemo {


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        //设置为eventtime事件类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置水印生成时间间隔100ms
        env.getConfig().setAutoWatermarkInterval(100);

        SingleOutputStreamOperator<Tuple2<String, Long>> dataStream = env
                .socketTextStream("127.0.0.1", 8888)
                .map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String s) throws Exception {

                        String[] split = s.split(",");
                        return new Tuple2<String, Long>(split[0], Long.parseLong(split[1]));
                    }
                })
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {
                    private Long currentTimeStamp = 0L;
                    //设置允许乱序时间
                    private Long maxOutOfOrderness = 0L;

                    @Override
                    public Watermark getCurrentWatermark() {

                        return new Watermark(currentTimeStamp - maxOutOfOrderness);
                    }

                    @Override
                    public long extractTimestamp(Tuple2<String, Long> s, long l) {
                        long timeStamp = s.f1;
                        System.err.println("currentTimeStamp:"+currentTimeStamp);
                        currentTimeStamp = Math.max(timeStamp, currentTimeStamp);
                        System.err.println(s + ",EventTime:" + timeStamp + ",watermark:" + (currentTimeStamp - maxOutOfOrderness));
                        return timeStamp;
                    }
                }).setParallelism(1);


        dataStream
                .keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .minBy(1)
                .print();

        env.execute("WaterMark Test Demo");

    }
}
