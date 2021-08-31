package com.chouc.flink.atguigu.example.business.topn.sql;

import com.chouc.flink.atguigu.example.business.topn.ClickEvent;
import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkSqlTopN {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setParallelism(1);
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(executionEnvironment);

//        String createTableSQL ="CREATE TABLE click_event (\n" +
//                    "  `userId` STRING,\n" +
//                    "  `adGroup` STRING,\n" +
//                    "  `pid` STRING,\n" +
//                    "  `noclick` INT,\n" +
//                    "  `click` INT,\n" +
//                    "  `ts` TIMESTAMP(3) METADATA FROM 'timestamp',\n" +
//                    "   WATERMARK FOR ts AS ts - INTERVAL '0' SECOND\n" +
//                    ") WITH (\n" +
//                    "  'connector' = 'kafka',\n" +
//                    "  'topic' = 'flink_learning',\n" +
//                    "  'properties.bootstrap.servers' = 's1:9092',\n" +
//                    "  'properties.group.id' = 'testGroup',\n" +
//                    "  'scan.startup.mode' = 'latest-offset',\n" +
//                    "  'format' = 'csv'\n" +
//                    ")";
//        streamTableEnvironment.executeSql(createTableSQL);


        SingleOutputStreamOperator<ClickEvent> clickEvent = StreamSourceUtils.getKafkaStream(executionEnvironment, "flink_learning")
                .flatMap((FlatMapFunction<StreamSourceUtils.CustomRecord, ClickEvent>) (value, out) -> {
                    try {
                        String line = value.getValue();
                        String[] split = line.split(",");
                        out.collect(new ClickEvent(split[0], split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]), value.getTimestamp()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarksAdapter.Strategy<>(new BoundedOutOfOrdernessTimestampExtractor<ClickEvent>(Time.seconds(0)) {
                    @Override
                    public long extractTimestamp(ClickEvent element) {
                        return element.getTimestamp();
                    }
                }));


        streamTableEnvironment.createTemporaryView("click_event", clickEvent, $("userId"), $("adGroup"), $("pid"), $("noclick"), $("click"), $("timestamp").rowtime());

        String queryFromKafka = "select adGroup,end_win,cnt,rnum from\n" +
                "    (\n" +
                "    select adGroup,end_win,cnt,row_number() over(partition by end_win order by cnt desc) as rnum \n" +
                "    from\n" +
                "        (\n" +
                "            select adGroup,HOP_END(`timestamp`, interval '30' second, interval '30' second) as end_win,count(1) as cnt\n" +
                "            from click_event \n" +
                "            group by adGroup,HOP(`timestamp`, interval '30' second, interval '30' second)\n" +
                "        )\n" +
                "    )\n" +
                "where rnum < 10";


        TableResult tableResult = streamTableEnvironment.executeSql(queryFromKafka);
        tableResult.print();

    }
}
