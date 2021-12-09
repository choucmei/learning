package chouc.spark.dstream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
 * @Title: SocketDstream
 * @Package chouc.spark.dstream
 * @Description:
 * @author chouc
 * @date 2/25/20
 * @version V1.0
 */
object SocketDstreamTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/SocketDstreamTest")
    val lines = ssc.socketTextStream("127.0.0.1", 9999).cache()
    val mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
      val sum = one.getOrElse(0) + state.getOption.getOrElse(0)
      val output = (word, sum)
      state.update(sum)
      output
    }

    val stateDstream = lines.flatMap(_.split(" ")).map((_, 1)).mapWithState(
      StateSpec.function(mappingFunc))
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }


}
