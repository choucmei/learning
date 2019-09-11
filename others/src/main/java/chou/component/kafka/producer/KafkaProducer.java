package chou.component.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: KafkaProducer
 * @Package chou.component.kafka.producer
 * @Description:
 * @date 9/4/19
 */
public class KafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();

        System.out.println(-8 >> 1);
        System.out.println(-8 >>> 1);

        props.put("zk.connect", "master01:2181");
        props.put("bootstrap.servers", "master01:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);
        for (int i = 0; i < 10000; i++) {
            producer.send(new ProducerRecord<String, String>("bigscreen", "{\"aid\":\"aid\",\"sessionid\":\"sessionid\"}{\"server_time\":\""+System.currentTimeMillis()+"\"}"));
            System.out.println("1");
        }
        producer.flush();
        producer.close();
    }
}
