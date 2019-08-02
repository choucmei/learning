package chouc.spark.structure.streaming


import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}

import scala.collection.JavaConverters._

/**
  * @Title: KafkaSource
  * @Package chouc.spark.structure.streaming
  * @Description:
  * @author chouc
  * @date 7/29/19
  * @version V1.0
  */
object KafkaSource {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val df = spark.read.format("kafka").option("kafka.bootstrap.servers", "smaster01:9092").option("subscribe", "impression,terminalerror,terminalclick,adrequest,adflow,leftflow,rtbflow").load()
    df.foreach(row => {
      println(s"kafka time:${row.getAs[Long]("timestamp")},offset:${row.getAs[Long]("offset")} ,partition:${row.getAs[Int]("partition")}")
    })
  }
}
