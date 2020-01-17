package chouc.spark.streaming

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * @Title: StructStreaming
  * @Package chouc.spark.rdd
  * @Description:
  * @author chouc
  * @date 11/29/19
  * @version V1.0
  */
object StructStreaming {
  def main(args: Array[String]): Unit = {
    val props = new Properties
    props.put("zk.connect", "master01:2181")
    props.put("bootstrap.servers", "master01:9092")
    props.put("acks", "all")
    //    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    //    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props.put("serializer.class", "kafka.serializer.DefaultEncoder")
    val producer = new KafkaProducer[String, String](props)
    for (i <- (1 until 10)){
      producer.send(new ProducerRecord[String, String]("test_topic", s"xiaomin2,w,60,${System.currentTimeMillis()/1000}"))
      producer.send(new ProducerRecord[String, String]("test_topic", s"xiaomin1,m,60,${System.currentTimeMillis()/1000}"))
      producer.send(new ProducerRecord[String, String]("test_topic", s"xiaomin3,w,60,${System.currentTimeMillis()/1000}"))

    }
    producer.flush()
    producer.close()


  }
}
