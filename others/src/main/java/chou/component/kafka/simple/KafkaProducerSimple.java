package chou.component.kafka.simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *  get recevier offset cmd:
 *  kafka-run-class kafka.tools.GetOffsetShell --broker-list s1:9092  --topic test_mxb
 *
 */
public class KafkaProducerSimple {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "s1:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "chou.component.kafka.simple.MyLogPartitioner");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        int threadNum = 2;
        long interval = 200l;
        String topic = "test_mxb";

        AtomicInteger idGetter = new AtomicInteger(threadNum);
        ExecutorService executorService = new ThreadPoolExecutor(threadNum,threadNum,
                0L, TimeUnit.HOURS,
                new ArrayBlockingQueue(10),Executors.defaultThreadFactory());
        while (idGetter.get()>0){
            executorService.execute(new SendThread(props,topic,idGetter,interval));
        }

    }

    static class SendThread extends Thread {
        Properties props;
        Producer<String, String> producer;
        String topic;
        int id;
        long interval;

        public SendThread(Properties props,String topic,AtomicInteger idGetter,long interval) {
            this.topic = topic;
            this.props = props;
            this.producer = new KafkaProducer<String, String>(props);
            this.id = idGetter.getAndDecrement();
            this.interval = interval;
        }

        @Override
        public void run() {
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    producer.flush();
                    producer.close();
                }
            });

            while (true){
                producer.send(new ProducerRecord<String, String>(topic,"key_"+ id, UUID.randomUUID().toString()));
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                producer.flush();
//                producer.close();
                System.out.println(this.id+" p ");
            }
        }
    }
}
