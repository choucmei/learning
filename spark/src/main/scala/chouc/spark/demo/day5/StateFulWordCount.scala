package chouc.spark.demo.day5

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object StateFulWordCount {


  //Seq这个批次某个单词的次数
  //Option[Int]：以前的结果

  //分好组的数据
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.map{ case(word, current_count, history_count) => (word, current_count.sum + history_count.getOrElse(0)) }
  }

  def main(args: Array[String]) {
//    LoggerLevels.setStreamingLogLevels()
    //StreamingContext
    val conf = new SparkConf().setAppName("StateFulWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //updateStateByKey必须设置setCheckpointDir
    sc.setCheckpointDir("c://ck")
    val ssc = new StreamingContext(sc, Seconds(5))

    val ds = ssc.socketTextStream("172.16.0.11", 8888)
    //DStream是一个特殊的RDD
    //hello tom hello jerry
    val result = ds.flatMap(_.split(" ")).map((_, 1)).updateStateByKey(updateFunc, new HashPartitioner(sc.defaultParallelism), true)

    result.print()

    ssc.start()

    ssc.awaitTermination()

  }
}
