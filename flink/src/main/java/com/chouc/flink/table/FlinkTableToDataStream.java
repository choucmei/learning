package com.chouc.flink.table;

import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkTableToDataStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStreamSource<StreamSourceUtils.CustomRecord> kafkaStream = StreamSourceUtils.getKafkaStream();
        Table sourceTable = streamTableEnvironment.fromDataStream(kafkaStream);
        Table resultTable = sourceTable.groupBy($("partition")).select($("partition"), $("partition").count().as("count"));

        // doesn't support consuming update changes which is produced by node GroupAggregate(groupBy=[partition], select=[partition, COUNT(partition) AS EXPR$0])
        DataStream<Row> rowDataStream1 = streamTableEnvironment.toDataStream(resultTable);

        // doesn't support consuming update changes which is produced by node GroupAggregate(groupBy=[partition], select=[partition, COUNT(partition) AS EXPR$0])
        DataStream<Row> rowDataStream2 = streamTableEnvironment.toAppendStream(resultTable,Row.class);

        DataStream<Tuple2<Boolean, Row>> rowDataStream3 = streamTableEnvironment.toRetractStream(resultTable,Row.class);


        rowDataStream3.print();

        streamExecutionEnvironment.execute();
    }
}
