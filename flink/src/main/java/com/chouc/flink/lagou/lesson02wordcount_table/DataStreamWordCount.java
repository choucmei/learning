package com.chouc.flink.lagou.lesson02wordcount_table;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: DataStreamWordCount
 * @Package com.chouc.flink.lagou.lession02wordcount_table
 * @Description:
 * @date 2020/8/18
 */
public class DataStreamWordCount {

    public static class EntityWC {
        public String word;
        public Integer count;

        public EntityWC() {
        }

        public EntityWC(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public Integer getCount() {
            return count;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "EntityWC{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<String> streamSource = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 1));
        DataStream<EntityWC> out = streamSource.flatMap(new RichFlatMapFunction<String, EntityWC>() {
            @Override
            public void flatMap(String value, Collector<EntityWC> out) throws Exception {
                for (String s : value.split(" ")) {
                    out.collect(new EntityWC(s,1));
                }
            }
        });
        out.keyBy(EntityWC::getWord).timeWindow(Time.seconds(10))
                .sum("count").print();
        env.execute();
    }
}
