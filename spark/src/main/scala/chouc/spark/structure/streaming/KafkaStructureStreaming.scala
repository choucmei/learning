package chouc.spark.structure.streaming

import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}

/**
  * @Title: KafkaStructureStreaming
  * @Package chouc.spark.structure.streaming
  * @Description:
  * @author chouc
  * @date 7/31/19
  * @version V1.0
  */
class KafkaStructureStreaming {

}

object KafkaStructureStreaming {
  val spark = SparkSession.builder().master("local[*]").getOrCreate()
  val df = spark.readStream.format("kafka")
    .option("kafka.bootstrap.servers", "smaster01:9092")
    .option("subscribe", "impression,terminalerror,terminalclick,adrequest,adflow,leftflow,rtbflow")
    .option("startingOffsets", "latest")
    .load()

  val q = df.writeStream.foreach(new ForeachWriter[Row]() {
    override def open(partitionId: Long, version: Long): Boolean = {
      println(s"partitionId:${partitionId} version:${version}")
      true
    }

    override def process(value: Row): Unit = {

      val time: String = value.getAs[String]("timestamp")

      println(time)
    }

    override def close(errorOrNull: Throwable): Unit = {

    }
  }).start()
  q.awaitTermination()
}