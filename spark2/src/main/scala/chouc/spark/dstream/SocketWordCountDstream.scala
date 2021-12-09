package chouc.spark.dstream

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SocketWordCountDstream {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("SocketWordCountDstream")
      .setMaster("local[3]")
    val sparkStreamContext = new StreamingContext(sparkConf,Seconds(5))
    val sparkContext = sparkStreamContext.sparkContext
    sparkContext.setLogLevel("WARN")
    sparkStreamContext.checkpoint("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\checkpoint")
    val dstream = sparkStreamContext.socketTextStream("localhost",9090)
    val v = dstream.flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey((pre,cur)=>{
        pre + cur
      })
    v.foreachRDD(rdd => {
      println(s"TIME ${System.currentTimeMillis()/1000}")
      rdd.foreach(println(_))
    })
    sparkStreamContext.start()
    sparkStreamContext.awaitTermination()
  }
}
