package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FlinkTableFromDataStream {
    public static void main(String[] args) {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<String> socketStream = StreamSourceUtils.getSocketStream();
        streamTableEnvironment.fromDataStream(socketStream).execute().print();
    }
}
