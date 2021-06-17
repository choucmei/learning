package com.chouc.flink.elasticsearch;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Arrays;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkElsasticsearchSink
 * @Package com.chouc.flink.elasticsearch
 * @Description:
 * @date 2021/5/13
 */
public class FlinkElsasticsearchSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> dataStreamSource = executionEnvironment.addSource(new SocketTextStreamFunction("localhost", 8888, "\n", 3));

        ElasticsearchSink.Builder elasticsearchSinkBuilder = new ElasticsearchSink.Builder(Arrays.asList(new HttpHost("localhost", 9200))
                , new ElasticsearchSinkFunction<String>() {
            @Override
            public void process(String jsonString, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
                IndexRequest indexRequest = new IndexRequest();
                indexRequest.index("users");
                indexRequest.source(jsonString, XContentType.JSON);
                requestIndexer.add(indexRequest);
            }
        });
        elasticsearchSinkBuilder.setBulkFlushInterval(1000);

        dataStreamSource.addSink(elasticsearchSinkBuilder.build());
        System.out.println(1);
        System.out.println(2);
        executionEnvironment.execute("flink2elasticsearch");

    }
}
