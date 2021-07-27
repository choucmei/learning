package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkTableTemporaryView {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<String> socketStream = StreamSourceUtils.getSocketStream();
//        streamTableEnvironment.createTemporaryView("socket_view", socketStream);

//        streamTableEnvironment.createTemporaryView("socket_view", socketStream, Schema.newBuilder()
//                .column("f0", "String")
//                .build());

        streamTableEnvironment.createTemporaryView("socket_view", socketStream, $("col"));

        streamTableEnvironment.executeSql("select * from socket_view").print();
    }
}
