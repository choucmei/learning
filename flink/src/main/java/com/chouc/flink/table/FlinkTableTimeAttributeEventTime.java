package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.e;

// https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/dev/table/concepts/time_attributes/
public class FlinkTableTimeAttributeEventTime {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<StreamSourceUtils.CustomRecord> kafkaStream = StreamSourceUtils.getKafkaStream();

        /**
         * https://ci.apache.org/projects/flink/flink-docs-release-1.13/zh/docs/dev/table/concepts/time_attributes/#%e5%9c%a8-datastream-%e5%88%b0-table-%e8%bd%ac%e6%8d%a2%e6%97%b6%e5%ae%9a%e4%b9%89-1
         * 这种方式会导致 rowtime 时间丢失时区
         */
        Table table1 = streamTableEnvironment.fromDataStream(kafkaStream, $("offset"), $("value"), $("timestamp"), $("rt").rowtime());


        Table table2 =
                streamTableEnvironment.fromDataStream(
                        kafkaStream,
                        Schema.newBuilder()
                                .column("value","String")
                                .column("offset","bigint")
                                .column("timestamp","bigint")
                                .columnByExpression("rowtime", "TO_TIMESTAMP_LTZ(`timestamp`,3)")
                                .build());



        table2.execute().print();
    }
}
