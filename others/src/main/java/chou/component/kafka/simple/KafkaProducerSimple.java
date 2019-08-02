package chou.component.kafka.simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class KafkaProducerSimple {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();

//        props.put("zk.connect", "s1:2181,s2:2181,s3:2181");
//        props.put("bootstrap.servers", "192.168.1.208:9092,192.168.1.212:9092,192.168.1.211:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        SimpleDateFormat smft=new SimpleDateFormat("YYYY/MM/dd/  HH:mm:ss E");

        for (int i = 1; i <= 10; i++) {
            Thread.sleep(200);
            String nowString=smft.format(new Date());
            producer.send(new ProducerRecord<String, String>("mst","2e3e1f60-9d71-43c5-b61f-69a48607f7bc#FE,F8,03,30,30,31,41,E2,07,0A,12,10,1E,18,0C,06,01,A2,07,02,8F,0A,03,D4,07,04,E9,09,05,A6,03,06,53,03,07,15,0B,08,F7,02,09,75,04,0A,DE,04,0B,77,0A,0C,E8,03,01,42,03,04,45,06,1B,00,C0"));
            System.out.println( "number of send :"+nowString);
        }
        producer.flush();
        producer.close();

    }
}
