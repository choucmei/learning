package chouc.spark.demo.zhihu;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ZhihuProducer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props = new Properties();
        props.put("zk.connect", "c1:2181,c2:2181,c3:2181");
        props.put("bootstrap.servers", "c3:9092,c4:9092,c5:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        String pathname = "/Users/chouc/Desktop/zhihudata/Zhihu.log.2018-03-27"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File file = new File(pathname);//Text文件
        BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
        String s = null;
        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            Thread.sleep(200);
            producer.send(new ProducerRecord<String, String>("zhihu",s));
            System.out.println(" produce"+System.currentTimeMillis());
        }
        br.close();
//        producer.flush();
        producer.close();

    }
}
