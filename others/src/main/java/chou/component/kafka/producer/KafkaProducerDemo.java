package chou.component.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerDemo {

    public static void main(String[] args) {
        String topic = "";
        String bootstrapServers = "";
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
//        props.put("delivery.timeout.ms", 30000);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 1000 && producer != null; i++) {
            producer.send(new ProducerRecord<>(topic, String.valueOf(i), "value"), (record, exception) -> {
                if (exception != null) {
                    System.out.println(" 发送失败 ");
                    exception.printStackTrace();
                } else {
                    System.out.println("timestamp，topic，partition，offset: " + record.timestamp() + ", " + record.topic() + "," + record.partition() + " " + record.offset());
                }
            });
        }
        producer.flush();
        producer.close();
    }
}
