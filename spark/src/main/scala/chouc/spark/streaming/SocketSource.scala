package chouc.spark.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

import scala.concurrent.duration.{Duration, SECONDS}

object SocketSource {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession
      .builder()
      .master("local[*]")
      .getOrCreate()
    val df = sparkSession.readStream
      .format("socket")
      .option("host","localhost")
      .option("port","9999")
      .load()
    val streamingQuery = df.writeStream
      .format("console")
      .trigger(Trigger.ProcessingTime(Duration.apply(10, SECONDS)))
      .start()
    streamingQuery.awaitTermination()

    streamingQuery.stop()

    sparkSession.streams.active.foreach(_.stop())
  }
}
