package com.chouc.flink.lagou.lesson08window_time_watermark;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * @author chouc
 * @version V1.0
 * @Title: TimeDemo
 * @Package com.chouc.flink.lagou.lession08window_time_watermark
 * @Description:
 * @date 2020/8/26
 */
public class TimeDemo {
    static class MyData{
        private String record;
        private Long timestamp;
        public String getRecord() {
            return record;
        }
        public void setRecord(String record) {
            this.record = record;
        }
        public Long getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public MyData(String record, Long timestamp) {
            this.record = record;
            this.timestamp = timestamp;
        }
    }

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStreamSource<String> source = env.socketTextStream("local",8888);
        DataStream<MyData> mydata = source.map(v -> new MyData(v,System.currentTimeMillis()));
        mydata.assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<MyData>(){

            @Override
            public long extractTimestamp(MyData data, long l) {
                return data.getTimestamp();
            }

            @Nullable
            @Override
            public Watermark checkAndGetNextWatermark(MyData data, long extractedTimestamp) {
                return data.getRecord().startsWith("watermark") ? new Watermark(extractedTimestamp) : null;
            }
        });

    }
}
