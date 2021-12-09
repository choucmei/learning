package chouc.spark.dstream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @Title: SocketDstream
 * @Package chouc.spark.dstream
 * @Description:
 * @author chouc
 * @date 2/25/20
 * @version V1.0
 */
object SocketDstream {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[3]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(4))
    ssc.sparkContext.setLogLevel("WARN")
    val lines = ssc.socketTextStream("localhost", 9999)
//    ssc.checkpoint("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/SocketDstream")
//    val wordCounts = lines.flatMap(_.split(" ")).map((_, 1)).updateStateByKey[Int]((seq: Seq[Int], total: Option[Int]) => {
//      total match {
//        case Some(value) => Option(seq.sum + value)
//        case None => Option(seq.sum)
//      }
//    })
    lines.foreachRDD(rdd => {
      rdd.foreach(uprint(_))
    })
    ssc.start()
    ssc.awaitTermination()
  }

  val sin = new Sink

  def uprint(iterable: Iterator[String]): Unit = {
    sin.uprint(iterable)
  }

  def uprint(iterable: String): Unit = {
    sin.uprint(iterable)
  }
}

class Sink {
  def apply(): Sink = {
    println("进入伴生对象的 apply 方法中")
    new Sink()
  }

  println(s" ${Thread.currentThread().getId} *************** Sink")

  var value = 0

  def uprint(iterable: Iterator[String]): Unit = {
    println(s"${Thread.currentThread().getId} - value:${value}")
    iterable.foreach(println(_))
    value += 1
  }

  def uprint(v: String): Unit = {
    println(s"${Thread.currentThread().getId} - value:${value}")
    println(v)
    value += 1
  }


}