package com.chouc.flink.lagou.lesson25advance_watermark;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;

import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: AscendingTimestampExtractorDemo
 * @Package com.chouc.flink.lagou.lession25advance_watermark
 * @Description:
 * @date 7/30/20
 */
public class AscendingTimestampExtractorDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 配置wm 周期时间
        env.getConfig().setAutoWatermarkInterval(1000);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer("flink_learning", new CustomDeSerializationSchema(), properties);
        flinkKafkaConsumer.setStartFromLatest();
        flinkKafkaConsumer.assignTimestampsAndWatermarks(WatermarkStrategy.<ConsumerRecord<String, String>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<ConsumerRecord<String, String>>() {
                    @Override
                    public long extractTimestamp(ConsumerRecord<String, String> element, long recordTimestamp) {
                        // 提取事件事件
                        return element.timestamp();
                    }
                }));
        DataStreamSource<ConsumerRecord<String, String>> source = env.addSource(flinkKafkaConsumer);
        source.keyBy(new KeySelector<ConsumerRecord<String, String>, String>() {
            @Override
            public String getKey(ConsumerRecord<String, String> element) throws Exception {
                String[] values = element.value().split(",");
                return values[0];
            }
        }).window(TumblingEventTimeWindows.of(Time.seconds(5)))
            .aggregate(new AggregateFunction<ConsumerRecord<String, String>, String, String>() {
                @Override
                public String createAccumulator() {
                    return "";
                }

                @Override
                public String add(ConsumerRecord<String, String> value, String accumulator) {
                    return accumulator + "-" + value.value();
                }

                @Override
                public String getResult(String accumulator) {
                    return accumulator;
                }

                @Override
                public String merge(String a, String b) {
                    return a + "*" + b;
                }
            }).print();
        env.execute("kafka source");
    }
}

class CustomDeSerializationSchema implements KafkaDeserializationSchema<ConsumerRecord<String, String>> {

    //是否表示流的最后一条元素,设置为false，表示数据会源源不断地到来
    @Override
    public boolean isEndOfStream(ConsumerRecord<String, String> stringStringConsumerRecord) {
        return false;
    }

    //这里返回一个ConsumerRecord<String,String>类型的数据，除了原数据还包括topic，offset，partition等信息
    @Override
    public ConsumerRecord<String, String> deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        return new ConsumerRecord<String, String>(
                record.topic(),
                record.partition(),
                record.offset(),
                record.timestamp(),
                TimestampType.LOG_APPEND_TIME,
                -1L, -1, -1,
                new String(record.key() == null ? "".getBytes() : record.key()),
                new String(record.value())
        );
    }

    @Override
    public TypeInformation<ConsumerRecord<String, String>> getProducedType() {
        return TypeInformation.of(new TypeHint<ConsumerRecord<String, String>>() {
        });
    }
}

