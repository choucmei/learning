package chouc.spark.alarm

import java.util.Properties

import chouc.spark.alarm.FilterUntils.{dateFormat, timeFormat}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
 * @Title: SparkStateTest
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/2/23
 * @version V1.0
 */
class SparkDStreamDemo {

}

object SparkDStreamDemo {
  def main(args: Array[String]): Unit = {
    val ssc = StreamingContext.getOrCreate("file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName, run)
    ssc.start()
    ssc.awaitTermination()
    sys.ShutdownHookThread {
      ssc.stop(stopSparkContext = true, stopGracefully = true)
    }
  }

  def run(): StreamingContext = {
    val sparkConf = new SparkConf()
      .setAppName("SocketWordCountDstream")
      .setMaster("local[3]")
    sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    val startDateStr = "2020-01-01"
    val endDateStr = "2021-01-01"
    val startTimeStr = "08:00:00"
    val endTimeStr = "20:00:00"
    val startDate = dateFormat.get().parse(startDateStr)
    val endDate = dateFormat.get().parse(endDateStr)
    val startTime = timeFormat.get().parse(startTimeStr)
    val endTime = timeFormat.get().parse(endTimeStr)

    val sparkStreamContext = new StreamingContext(sparkSession.sparkContext, Seconds(5))
    val dstream = sparkStreamContext.socketTextStream("localhost", 9090)

    val KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
    val kafkaProducer: Broadcast[KafkaSink[String, String]] = {
      val kafkaProducerConfig = {
        val p = new Properties()
        p.setProperty("bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS)
        p.setProperty("key.serializer", classOf[StringSerializer].getName)
        p.setProperty("value.serializer", classOf[StringSerializer].getName)
        p
      }
      sparkStreamContext.sparkContext.broadcast(KafkaSink[String, String](kafkaProducerConfig))
    }
    val filter = FilterUntils.filterDateTime(startDate, endDate, startTime, endTime, _)


    val mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
      val sum = one.getOrElse(0) + state.getOption.getOrElse(0)
      val output = (word, sum)
      state.update(sum)
      output
    }
    dstream.map(str => {
      val values = str.split(",")
      (values(0), values(1), 1)
    }).filter(v => filter(v._2)).map(v => (v._1, v._3))
      .mapWithState(StateSpec.function(mappingFunc))
      .print()
    sparkStreamContext.checkpoint("file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName)
    sparkStreamContext
  }
}
