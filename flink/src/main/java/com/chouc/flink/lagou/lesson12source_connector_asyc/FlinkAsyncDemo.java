package com.chouc.flink.lagou.lesson12source_connector_asyc;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkAsyncDemo
 * @Package com.chouc.flink.lagou.lession12source_connector_asyc
 * @Description:
 * @date 2020/9/21
 */
public class FlinkAsyncDemo {
    static class AsyncDatabaseRequest extends RichAsyncFunction<String, Tuple2<String, String>> {
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
        }

        @Override
        public void close() throws Exception {
            super.close();
        }

        @Override
        public void asyncInvoke(String input, ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {

        }
    }
    public static void main(String[] args) {

    }
}
