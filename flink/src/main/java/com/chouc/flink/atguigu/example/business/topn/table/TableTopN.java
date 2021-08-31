package com.chouc.flink.atguigu.example.business.topn.table;

import com.chouc.flink.atguigu.example.business.topn.ClickEvent;
import com.chouc.flink.utils.StreamSourceUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.table.api.GroupWindow;
import org.apache.flink.table.api.Over;
import org.apache.flink.table.api.Slide;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;

public class TableTopN {
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setParallelism(1);
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(executionEnvironment);
        SingleOutputStreamOperator<ClickEvent> clickEvent = StreamSourceUtils.getKafkaStream(executionEnvironment, "flink_learning")
                .flatMap(new FlatMapFunction<StreamSourceUtils.CustomRecord, ClickEvent>() {
                    @Override
                    public void flatMap(StreamSourceUtils.CustomRecord value, Collector<ClickEvent> out) throws Exception {
                        try {
                            String line = value.getValue();
                            String[] split = line.split(",");
                            out.collect(new ClickEvent(split[0], split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]), value.getTimestamp()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarksAdapter.Strategy<>(new BoundedOutOfOrdernessTimestampExtractor<ClickEvent>(Time.seconds(0)) {
                    @Override
                    public long extractTimestamp(ClickEvent element) {
                        return element.getTimestamp();
                    }
                }));

        Table pre = streamTableEnvironment.fromDataStream(clickEvent, $("userId"), $("adGroup"), $("pid"), $("noclick"), $("click"), $("timestamp").rowtime())
                .window(Slide.over(lit(30).seconds()).every(lit(30).seconds()).on("timestamp").as("wd"))
                .groupBy($("adGroup"), $("wd"))
                .select($("adGroup"), $("userId").count().as("cnt"), $("wd").end().as("ewd"));
        streamTableEnvironment.createTemporaryView("pre",pre);
        String sql="select * from  (select *,row_number() over(partition by ewd order by cnt) as rnum from pre) ppre where rnum <=10 ";
        System.out.println(streamTableEnvironment.explainSql(sql));
//        streamTableEnvironment.executeSql(sql).print();

    }
}
