package chouc.spark.streaming

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.functions.{col, window}
import org.apache.spark.sql.streaming.OutputMode

/**
  * @Title: GenerateDataToKafka
  * @Package chouc.spark.rdd
  * @Description:
  * @author chouc
  * @date 11/29/19
  * @version V1.0
  */
object WindowFunc {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    import spark.implicits._
    val kafkaDf = spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "master01:9092")
      //      .option("startingOffsets", "earliest")
      .option("subscribe", "test_topic")
      .load().selectExpr("cast(value as String) as value")
    val df = kafkaDf.map(row => {
      val line = row.getAs[String]("value")
      line.split(",")
    }).filter(_.size >= 3).map(values => new P(values(0), values(1), values(2).toInt, values(3).toLong)).toDF().selectExpr("name","sex","age","cast(timestamp as Timestamp) as timestamp")


    val windowedCounts = df.groupBy(
      window(col("timestamp"), "2 minutes", "1 minutes"),
      $"name",$"sex"
    ).count()
    windowedCounts.printSchema()
    val windowQuery = windowedCounts.writeStream.outputMode(OutputMode.Complete()).foreachBatch((ds,index)=>{
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      ds.map(value =>{
        val window = value.getAs[GenericRowWithSchema]("window")
        val start = window.getAs[Timestamp]("start")
        val end = window.getAs[Timestamp]("end")
        val name = value.getAs[String]("name")
        val sex = value.getAs[String]("sex")
        val count = value.getAs[Long]("count")
        s" start:${simpleDateFormat.format(start)} end:${simpleDateFormat.format(end)}  name:$name  sex:$sex count:$count"
      }).write.text(s"/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/result/${simpleDateFormat.format(new Date())}")

    }).start()


    windowQuery.awaitTermination()
  }
}
case class P(name: String, sex: String, age: Int,timestamp: Long)


