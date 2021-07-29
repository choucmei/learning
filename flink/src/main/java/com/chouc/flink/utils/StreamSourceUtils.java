package com.chouc.flink.utils;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class StreamSourceUtils {

    public static DataStreamSource<String> getSocketStream() {
        return StreamExecutionEnvironment.getExecutionEnvironment().addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
    }

    public static DataStreamSource<CustomRecord> getKafkaStream() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<CustomRecord> source;
        source = KafkaSource.<CustomRecord>builder()
                .setBootstrapServers("s1:9092")
                .setTopics("user_behavior")
                .setGroupId("StreamSourceUtils")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setDeserializer(new KafkaRecordDeserializationSchema<CustomRecord>() {
                    @Override
                    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<CustomRecord> out) throws IOException {
                        out.collect(new CustomRecord(record.topic(), record.offset(), record.partition(), record.timestamp(), new String(record.key() == null ? "".getBytes() : record.key()), new String(record.value())));
                    }

                    @Override
                    public TypeInformation<CustomRecord> getProducedType() {
                        return TypeInformation.of(CustomRecord.class);
                    }
                })
                .build();
        return env.fromSource(source, WatermarkStrategy.noWatermarks(), "kafkasource");
    }

    public static class CustomRecord {
        private String topic;
        private long offset;
        private long partition;
        private long timestamp;
        private String key;
        private String value;

        public CustomRecord() {
        }

        public CustomRecord(String topic, long offset, long partition, long timestamp, String key, String value) {
            this.topic = topic;
            this.offset = offset;
            this.partition = partition;
            this.timestamp = timestamp;
            this.key = key;
            this.value = value;
        }

        public long getPartition() {
            return partition;
        }

        public void setPartition(long partition) {
            this.partition = partition;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
