package chou.component.kafka.simple;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsumerSimple  implements Runnable{
    private String consumeName;

    public KafkaConsumerSimple(String consumeName) {
        this.consumeName = consumeName;
    }

    @Override
    public void run() {
        Properties props = new Properties();
//        props.put("zk.connect", "s1:2181,s2:2181,s3:2181");
//        props.put("bootstrap.servers", "s1:9092");
        props.put("bootstrap.servers", "192.168.1.208:9092,192.168.1.212:9092,192.168.1.211:9092");
        props.put("acks", "all");
        props.put("group.id", "testmxb");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("partitioner.class", "chou.component.kafka.MyLogPartitioner");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        Collection<String> collection = new HashSet<String>();
        collection.add("zhihu");
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(collection);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("consumer:" + consumeName +"key:" + record.key() + "-value:" + record.value());
            }
        }
    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            System.out.println("========"+i);
            executorService.execute(new KafkaConsumerSimple("consume"+i));
        }

    }




}
