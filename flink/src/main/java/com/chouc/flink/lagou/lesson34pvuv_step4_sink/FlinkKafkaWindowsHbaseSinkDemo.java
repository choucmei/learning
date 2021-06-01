package com.chouc.flink.lagou.lesson34pvuv_step4_sink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chouc.flink.lagou.lesson32pvuv_step2_kafka_window.ClickEntity;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkEtlKafkaDemo
 * @Package com.chouc.flink.lagou.lesson31etl_kafka
 * @Description:
 * @date 2020/11/12
 */
public class FlinkKafkaWindowsHbaseSinkDemo {

    public static String MSQL_DRIVR = "";

    static class DateUtil {
        public static String timeStampToDate(Long timestamp) {
            ThreadLocal<SimpleDateFormat> threadLocal
                    = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            String format = threadLocal.get().format(new Date(timestamp));
            return format.substring(0, 10);
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
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();
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
                        return DateUtil.timeStampToDate(value.getTimestamp() * 1000);
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
                .trigger(new Trigger<ClickEntity, TimeWindow>() {

                    int interval = 5 * 1000;
                    /** When merging we take the lowest of all fire timestamps as the new fire timestamp. */
                    private final ReducingStateDescriptor<Long> stateDesc =
                            new ReducingStateDescriptor<>("fire-time", new ReduceFunction<Long>() {
                                @Override
                                public Long reduce(Long value1, Long value2) throws Exception {
                                    return Math.min(value1, value2);
                                }
                            }, LongSerializer.INSTANCE);

                    @Override
                    public TriggerResult onElement(ClickEntity element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
                        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);

                        timestamp = ctx.getCurrentProcessingTime();
                        System.out.println(new Date(timestamp).toLocaleString() + " ws " + new Date(window.getStart()).toLocaleString()
                                + " wd " + new Date(window.getEnd()).toLocaleString() + " ct ");

                        if (fireTimestamp.get() == null) {
                            long start = timestamp - (timestamp % interval);
                            long nextFireTimestamp = start + interval;

                            ctx.registerProcessingTimeTimer(nextFireTimestamp);

                            fireTimestamp.add(nextFireTimestamp);
                            return TriggerResult.CONTINUE;
                        }
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);

                        if (fireTimestamp.get().equals(time)) {
                            fireTimestamp.clear();
                            fireTimestamp.add(time + interval);
                            ctx.registerProcessingTimeTimer(time + interval);
                            return TriggerResult.FIRE;
                        }
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
                        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
                        long timestamp = fireTimestamp.get();
                        ctx.deleteProcessingTimeTimer(timestamp);
                        fireTimestamp.clear();
                    }

                    @Override
                    public boolean canMerge() {
                        return true;
                    }

                    @Override
                    public void onMerge(TimeWindow window,
                                        OnMergeContext ctx) {
                        ctx.mergePartitionedState(stateDesc);
                    }

                })
//                .evictor(TimeEvictor.of(Time.seconds(0), true))
                .process(new ProcessWindowFunction<ClickEntity, Tuple3<String, String, Integer>, String, TimeWindow>() {

                    private transient MapState<String, String> uvState;
                    private transient ValueState<Integer> pvState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        uvState = getRuntimeContext().getMapState(new MapStateDescriptor<>("uv", String.class, String.class));
                        pvState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("pv", Integer.class));
                    }

                    @Override
                    public void process(String s, Context context, Iterable<ClickEntity> elements, Collector<Tuple3<String, String, Integer>> out) throws Exception {
                        Integer pv = 0;
                        Iterator<ClickEntity> iterator = elements.iterator();
                        while (iterator.hasNext()) {
                            pv = pv + 1;
                            String userId = iterator.next().getUserId();
                            uvState.put(userId, null);
                        }


                        Integer uv = 0;
                        Iterator<String> uvIterator = uvState.keys().iterator();
                        while (uvIterator.hasNext()) {
                            String next = uvIterator.next();
                            uv = uv + 1;
                        }

                        Integer value = pvState.value();
                        if (null == value) {
                            pvState.update(pv);
                        } else {
                            pvState.update(value + pv);
                        }
                        System.out.println("pv" + pv);
                        out.collect(Tuple3.of(s, "uv", uv));
                        out.collect(Tuple3.of(s, "pv", pvState.value()));
                    }
                }).addSink(new RichSinkFunction<Tuple3<String, String, Integer>>() {
                        private transient Connection connection;
                        private transient Table table;
                        private transient List<Put> cache;
                        private transient String tableName = "database:pvuv_result";
                        private transient String family = "f";

                        @Override
                        public void open(Configuration parameters) throws Exception {
                            super.open(parameters);
                            org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
                            conf.set(HConstants.ZOOKEEPER_QUORUM, "localhost:2181");
                            connection = ConnectionFactory.createConnection(conf);
                            table = connection.getTable(TableName.valueOf(tableName));
                            cache = new ArrayList<>(100);
                        }

                        @Override
                        public void invoke(Tuple3<String, String, Integer> value, Context context) throws Exception {
                            Put put = new Put(value.f0.getBytes());
                            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(value.f1), Bytes.toBytes(value.f2));
                            cache.add(put);
                            if (cache.size() == 100) {
                                table.put(cache);
                                cache.clear();
                            }
                        }

                        @Override
                        public void close() throws Exception {
                            super.close();
                            table.close();
                            connection.close();
                        }
                    });


        env.execute();
    }

}
