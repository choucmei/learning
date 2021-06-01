package com.chouc.flink.lagou.lesson33pvuv_step3_value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chouc.flink.lagou.lesson32pvuv_step2_kafka_window.ClickEntity;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.text.SimpleDateFormat;
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
public class FlinkKafkaWindowsBitMapDemo {

    static class DateUtil {
        public static String timeStampToDate(Long timestamp){
            ThreadLocal<SimpleDateFormat> threadLocal
                    = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            String format = threadLocal.get().format(new Date(timestamp));
            return format.substring(0,10);
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // 检查点配置,如果要用到状态后端，那么必须配置
        env.setStateBackend(new MemoryStateBackend(true));
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
                .keyBy(new KeySelector<ClickEntity, String>() {
                    @Override
                    public String getKey(ClickEntity value) throws Exception {
                        return DateUtil.timeStampToDate(value.getTimestamp());
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(20)))
                .evictor(TimeEvictor.of(Time.seconds(0), true))
                .process(new ProcessWindowFunction<ClickEntity, Tuple3<String,String,Integer>, String, TimeWindow>() {
                    private transient ValueState<Integer> uvState;
                    private transient ValueState<Integer> pvState;
                    private transient ValueState<Roaring64NavigableMap> bitMapState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        uvState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("uv", Integer.class));
                        pvState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("pv", Integer.class));
                        bitMapState = getRuntimeContext().getState(new ValueStateDescriptor<Roaring64NavigableMap>("bitmap", Roaring64NavigableMap.class));
                    }

                    @Override
                    public void process(String s, Context context, Iterable<ClickEntity> elements, Collector<Tuple3<String,String, Integer>> out) throws Exception {

                        Roaring64NavigableMap bitMap = bitMapState.value();
                        if (bitMap == null){
                            bitMap = new Roaring64NavigableMap();
                        }
                        Integer pv = 0;
                        Iterator<ClickEntity> iterator = elements.iterator();
                        while (iterator.hasNext()){
                            pv = pv + 1;
                            String userId = iterator.next().getUserId();
                            bitMap.add(Long.valueOf(userId));
                        }

                        bitMapState.update(bitMap);
                        Integer uv = bitMap.getIntCardinality();
                        out.collect(Tuple3.of(s,"uv",uv));
                        out.collect(Tuple3.of(s,"pv",uv));
                    }
                }).print();

        env.execute();
    }
}