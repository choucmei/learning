package com.chouc.flink.cdc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.debezium.StringDebeziumDeserializationSchema;

public class SingleFlinkReadBinlog {
    public static void main(String[] args) throws Exception {
        SourceFunction<String> sourceFunction =
            MySqlSource.<String>builder()
                .hostname("s4")
                .port(3306)
                // monitor all tables under inventory database
                .databaseList("test")
                .username("root")
                .password("123456")
                .deserializer(new StringDebeziumDeserializationSchema())
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(sourceFunction).print().setParallelism(1);

        env.execute("Print MySQL Snapshot + Binlog");
    }
}
