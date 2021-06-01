package com.chouc.flink.lagou.lesson32pvuv_step2_kafka_window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Date;
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
public class FlinkKafkaWindowsDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
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
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(10)))
                .trigger(new Trigger<ClickEntity, TimeWindow>() {


                    @Override
                    public TriggerResult onElement(ClickEntity element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
                        System.out.println("************onElement:" + element.getUserId()+"-"+element.getTimestamp()+"-"+element.getAction()
                                +" Window Start :"+new Date(window.getStart()).toLocaleString()+" Window End:"+new Date(window.getEnd()).toLocaleString()+
                                " now :"+new Date(timestamp).toLocaleString());
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        System.out.println("************onProcessingTime:" + time +" Window Start :"+new Date(window.getStart()).toLocaleString()+" Window End:"+new Date(window.getEnd()).toLocaleString());
                        return TriggerResult.FIRE;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        System.out.println("************onEventTime:" + time+" Window Start :"+new Date(window.getStart()).toLocaleString()+" Window End:"+new Date(window.getEnd()).toLocaleString());
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
                        System.out.println("************clear:");
                    }
                }).process(new ProcessAllWindowFunction<ClickEntity, ClickEntity, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<ClickEntity> elements, Collector<ClickEntity> out) throws Exception {
                        Iterator<ClickEntity> it = elements.iterator();
                        while (it.hasNext()){
                            out.collect(it.next());
                        }
                    }
                }).print();
        env.execute();
    }
}

class UserActionFilter implements FilterFunction<String> {
    @Override
    public boolean filter(String input) throws Exception {
        return input.contains("CLICK") && input.startsWith("{") && input.endsWith("}");
    }
}

class MyFlatMapFunction implements FlatMapFunction<String, String> {

    @Override
    public void flatMap(String input, Collector<String> out) throws Exception {
        JSONObject jsonObject = JSON.parseObject(input);
        String user_id = jsonObject.getString("user_id");
        String action = jsonObject.getString("action");
        Long timestamp = jsonObject.getLong("timestamp");
        if (!StringUtils.isEmpty(user_id) || !StringUtils.isEmpty(action)) {
            ClickEntity userClick = new ClickEntity(user_id, timestamp, action);
            userClick.setUserId(user_id);
            userClick.setTimestamp(timestamp);
            userClick.setAction(action);
            out.collect(JSON.toJSONString(userClick));
        }
    }
}

class UserActionProcessFunction extends ProcessFunction<String, String> {

    @Override
    public void processElement(String input, Context ctx, Collector<String> out) throws Exception {
        if (!input.contains("CLICK") || input.startsWith("{") || input.endsWith("}")) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(input);
        String user_id = jsonObject.getString("user_id");
        String action = jsonObject.getString("action");
        Long timestamp = jsonObject.getLong("timestamp");
        if (!StringUtils.isEmpty(user_id) || !StringUtils.isEmpty(action)) {
            ClickEntity userClick = new ClickEntity(user_id, timestamp, action);
            userClick.setUserId(user_id);
            userClick.setTimestamp(timestamp);
            userClick.setAction(action);
            out.collect(JSON.toJSONString(userClick));
        }
    }
}