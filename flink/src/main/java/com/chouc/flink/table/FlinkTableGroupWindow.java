package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;
import java.time.ZoneId;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;


// https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/dev/table/tableapi/#group-windows
public class FlinkTableGroupWindow {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);

//        streamTableEnvironment.getConfig().setLocalTimeZone(ZoneId.of("Asia/Shanghai"));

//        streamTableEnvironment.getConfig().setLocalTimeZone(ZoneId.of("UTC"));

        // set to Los_Angeles time zone
//        streamTableEnvironment.getConfig().setLocalTimeZone(ZoneId.of("America/Los_Angeles"));


//        DataStreamSource<StreamSourceUtils.CustomRecord> socketStream = StreamSourceUtils.getKafkaStream("user_behavior");
        SingleOutputStreamOperator<Tuple3<String, Long, Long>> socketStream = StreamSourceUtils.getSocketStream().map(new RichMapFunction<String, Tuple3<String, Long, Long>>() {
            @Override
            public Tuple3<String, Long, Long> map(String value) throws Exception {
                String[] values = value.split(",");
                return Tuple3.of(values[0], Long.parseLong(values[1]), Long.parseLong(values[2]));
            }
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, Long, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(0)).withTimestampAssigner((SerializableTimestampAssigner<Tuple3<String, Long, Long>>) (element, recordTimestamp) -> {
            System.out.println(Thread.currentThread().getName()+" value :"+element.f0+" - eventime "+element.f2);
            return element.f2;
        }));


        Table table = streamTableEnvironment.fromDataStream(socketStream, $("value"), $("partition"), $("f2").rowtime());

        // table API
//        table.window(Tumble.over(lit(5).seconds()).on($("f2")).as("t_w"))
//                .groupBy($("partition"), $("t_w"))
//                .select($("partition"), $("partition").count().as("partition_count"), $("t_w").start().as("w_start"), $("t_w").end().as("w_end"))
//                .execute();
//                .print();

        // SQL

        streamTableEnvironment.createTemporaryView("tb",table);
        String sql ="SELECT `partition`, count(`partition`) as partition_count,tumble_start(f2, interval '5' second), tumble_end(f2, interval '5' second) \n" +
                    "  FROM tb " +
                    "  GROUP BY `partition`,tumble(f2, interval '5' second)";

        streamTableEnvironment.executeSql(sql).print();



    }
}
