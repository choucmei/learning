package com.chouc.flink.atguigu.example.business.uv;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.tuple.Tuple7;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.util.Collector;

public class TextToText {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> stringDataStreamSource = executionEnvironment.readTextFile("C:\\Users\\chouc\\Desktop\\ym");

        stringDataStreamSource.flatMap(new FlatMapFunction<String, String>() {
            Long t = 1630240110000L;

            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] split = value.split(",");
                try {
//                    name,src,create_company,report_company,prov,year,price
                    out.collect(Tuple7.of(split[0], split[1], split[2], split[3], split[4], Double.parseDouble(split[6]), t).toString().replace(")","").replace("(",""));
                    t += 1000l;
                } catch (Exception e) {
                    System.out.println(value);
                }
            }
        }).setParallelism(1).addSink(StreamingFileSink.forRowFormat(new Path("C:\\Users\\chouc\\Desktop\\ym1\\"), new SimpleStringEncoder<String>()
        ).build()).setParallelism(1);
        executionEnvironment.execute();
    }
}
