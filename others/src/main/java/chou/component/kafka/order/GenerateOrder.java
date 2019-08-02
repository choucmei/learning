package chou.component.kafka.order;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class GenerateOrder {

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();

        props.put("zk.connect", "s1:2181,s2:2181,s3:2181");
        props.put("bootstrap.servers", "s1:9092,s4:9092,s5:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer producer = new KafkaProducer<String,String>(props);
        for (int i=1;i<10000;i++){
            Thread.sleep(200);
            String tem = new OrderBean().random();
            producer.send(new ProducerRecord<String,String>("orderMq",tem));
            System.out.println("sent:"+tem);
        }
        producer.flush();
        producer.close();
    }

}
