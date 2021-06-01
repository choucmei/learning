package com.chouc.flink.lagou.lesson33pvuv_step3_value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chouc.flink.lagou.lesson32pvuv_step2_kafka_window.ClickEntity;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkEtlKafkaDemo
 * @Package com.chouc.flink.lagou.lesson31etl_kafka
 * @Description:
 * @date 2020/11/12
 */
public class FlinkKafkaWindowsSimpleDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // 检查点配置,如果要用到状态后端，那么必须配置
        env.setStateBackend(new MemoryStateBackend(true));
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        env.enableCheckpointing(5000);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        FlinkKafkaConsumer<String> source = new FlinkKafkaConsumer<String>("flink_learning", new SimpleStringSchema(), properties);
        source.setStartFromLatest();
        DataStream<ClickEntity> dataStream = env.addSource(source)
                .name("log_user_action")
                .map(ms -> {
                    JSONObject record = JSON.parseObject(ms);
                    return new ClickEntity(
                            record.getString("userId"),
                            record.getLong("timestamp"),
                            record.getString("action")
                    );
                })
                .returns(TypeInformation.of(ClickEntity.class));
        dataStream.assignTimestampsAndWatermarks(WatermarkStrategy.<ClickEntity>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SerializableTimestampAssigner<ClickEntity>() {
                    @Override
                    public long extractTimestamp(ClickEntity element, long recordTimestamp) {
                        return element.getTimestamp() * 1000;
                    }
                }))
                .windowAll(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(20)))
                .process(new ProcessAllWindowFunction<ClickEntity, Tuple2<String, Integer>, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<ClickEntity> elements, Collector<Tuple2<String, Integer>> out) throws Exception {
                        HashSet<String> uv = new HashSet<>();
                        Integer pv = 0;
                        Iterator<ClickEntity> it = elements.iterator();
                        while (it.hasNext()) {
                            ClickEntity clickEntity = it.next();
                            uv.add(clickEntity.getUserId());
                            pv += 1;
                        }
                        out.collect(Tuple2.of("pv", pv));
                        out.collect(Tuple2.of("uv", uv.size()));
                    }
                }).print();

        env.execute();
    }
}