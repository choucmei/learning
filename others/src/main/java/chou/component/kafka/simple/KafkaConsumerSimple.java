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
        props.put("bootstrap.servers", "s1:9092");
        props.put("group.id", "test_mxb");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        Collection<String> collection = new HashSet<String>();
        collection.add("test_mxb");
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(collection);
        for (int i = 0 ;i <10;i++){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("consumer:" + consumeName +"key:" + record.key() + "-value:" + record.value());
            }
        }
        consumer.commitAsync();
        System.out.println("commit");
    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executorService.execute(new KafkaConsumerSimple("consume"+i));
        }


    }




}
