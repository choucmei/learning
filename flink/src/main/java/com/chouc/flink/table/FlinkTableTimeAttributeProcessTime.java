package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;
// https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/dev/table/concepts/time_attributes/
public class FlinkTableTimeAttributeProcessTime {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<String> socketStream = StreamSourceUtils.getSocketStream();

        Table table1 = streamTableEnvironment.fromDataStream(socketStream, $("col"), $("pt").proctime());


        Table table2 =
                streamTableEnvironment.fromDataStream(
                        socketStream,
                        Schema.newBuilder()
                                .column("f0","String")
                                .columnByExpression("proc_time", "PROCTIME()")
                                .build());

        table2.execute().print();
    }
}
