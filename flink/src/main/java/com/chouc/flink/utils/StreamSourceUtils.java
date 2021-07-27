package com.chouc.flink.utils;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;

public class StreamSourceUtils {
    public static DataStreamSource<String> getSocketStream(){
        return StreamExecutionEnvironment.getExecutionEnvironment().addSource(new SocketTextStreamFunction("localhost",9999,"\n",3));
    }
}
