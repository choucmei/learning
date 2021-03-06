package chouc.spark.dstream

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
  * @Title: KafkaDstream
  * @Package chouc.spark.dstream
  * @Description:
  * @author chouc
  * @date 1/15/20
  * @version V1.0
  */
object KafkaDirectDstream {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("KafkaDirectDstream")
    sparkConf.setMaster("local[*]")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "1")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(2))
    streamingContext.sparkContext.setLogLevel("ERROR")
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "s1:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "p1",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array("test_mxb")
    streamingContext.checkpoint("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\checkpoint\\KafkaDirectDstream")
    val dstream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    dstream.map(record => (record.key, record.value, record.partition(), record.offset()))
      .foreachRDD(rdd => {
        println(s" Time: ${System.currentTimeMillis() / 1000}")

        rdd.mapPartitionsWithIndex((p, it) => {
          println(s" partition:${p} count:${it.count(x=>true)}")
          it
        }).foreachPartition(v=>null)

      })
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
