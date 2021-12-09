package chouc.spark.dstream

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Durations, StreamingContext}

object DstreamCheckpoint {
  def main(args: Array[String]): Unit = {
    val ssc = StreamingContext.getOrCreate("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\checkpoint\\DstreamCheckpoint",functionToCreateContext)
    ssc.sparkContext.setLogLevel("ERROR")
    ssc.start()
    ssc.awaitTermination()
  }

  def functionToCreateContext(): StreamingContext = {
    println("functionToCreateContext invoke")
    val sparkConf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("DstreamCheckpoint")
    val ssc = new StreamingContext(sparkConf,Durations.seconds(2))

//    ssc.sparkContext.ui

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "s1:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "qq",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array("test_mxb")
    val dstream = KafkaUtils.createDirectStream(ssc,PreferConsistent,Subscribe[String, String](topics, kafkaParams))
    dstream.map(record => (record.key, record.value,record.partition(),record.offset()))
      .foreachRDD(rdd => {
        println(s" Time: ${System.currentTimeMillis()/1000}")
        rdd.mapPartitionsWithIndex((p,it)=>{
          it.map(it=>{
            (p,it._1,it._2,it._3,it._4)
          })
        }).foreachPartition(it=>{
          it.foreach(v=>{
            println(s"spark partition:${v._1}  kafka parititon:${v._4} key:${v._2} value:${v._3} offset:${v._5}")
          })
        })
      })
    ssc.checkpoint("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\checkpoint\\DstreamCheckpoint")
    ssc
  }
}
