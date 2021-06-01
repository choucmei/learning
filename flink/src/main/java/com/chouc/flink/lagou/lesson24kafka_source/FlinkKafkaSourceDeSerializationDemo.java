package com.chouc.flink.lagou.lesson24kafka_source;

import org.apache.commons.collections.map.HashedMap;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkKafkaSourceDeSerializationDemo
 * @Package com.chouc.flink.lagou.lession23kafka_sink
 * @Description:
 * @date 2020/10/26
 */
public class FlinkKafkaSourceDeSerializationDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.createLocalEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // 动态分区检测
        properties.setProperty(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "10");
//        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<>("flink_learning", new CustomDeSerializationSchema(), properties);

        // topic 规则，新建topic 不重启也会识别
        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<>(Pattern.compile("^flink_([A-Za-z0-9]*)$"), new CustomDeSerializationSchema(), properties);

        // 指定
        Map<KafkaTopicPartition, Long> offsets = new HashedMap();
        offsets.put(new KafkaTopicPartition("flink_learning", 0), 2L);
        offsets.put(new KafkaTopicPartition("flink_learning", 1), 2L);
        offsets.put(new KafkaTopicPartition("flink_learning", 2), 2L);
        consumer.setStartFromSpecificOffsets(offsets);



        consumer.setStartFromEarliest();
        DataStreamSource<ConsumerRecord<String, String>> ds = sEnv.addSource(consumer);
        ds.print();
        sEnv.execute();
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
                new String(record.key()==null?  "".getBytes() :record.key()),
                new String(record.value())
        );
    }

    @Override
    public TypeInformation<ConsumerRecord<String, String>> getProducedType() {
        return TypeInformation.of(new TypeHint<ConsumerRecord<String, String>>(){});
    }
}
