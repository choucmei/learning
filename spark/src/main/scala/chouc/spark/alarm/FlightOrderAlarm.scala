package chouc.spark.alarm

import java.util.Properties

import chouc.spark.alarm.FilterUntils.{dateFormat, timeFormat}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Title: AirflyOrderAlarm
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/3/1
 * @version V1.0
 */
object FlightOrderAlarm {
  def main(args: Array[String]): Unit = {
    val SPARK_CK_DIR = "file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName
    val condition = ""
    val ssc = StreamingContext.getOrCreate(SPARK_CK_DIR, () => creatingStreamingContextFunc(args, condition))
    ssc.start()
    ssc.awaitTermination()
    sys.ShutdownHookThread {
      ssc.stop(stopSparkContext = true, stopGracefully = true)
    }
  }

  def creatingStreamingContextFunc(args: Array[String], condidtion: String): StreamingContext = {
    //告警参数
    val startDateStr = ""
    val endDateStr = ""
    val startTimeStr = ""
    val endTimeStr = ""
    val srcRegions = "".split(",")
    val tgtRegions = "".split(",")
    val srcRegionNames = "".split(",")
    val tgtRegionNames = "".split(",")

    // 配置参数
    val KAFKA_BOOTSTRAP_SERVERS = ""
    val KAFKA_COMSUMER_TOPICS = "".split(",")
    val KAFKA_COMSUMER_SINK_TOPICS = ""
    val KAFKA_COMSUMER_GROUPID = this.getClass.getName
    val SPARK_CK_DIR = ""


    //stream context
    val sparkConf = new SparkConf()
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(2))
    streamingContext.sparkContext.setLogLevel("ERROR")
    streamingContext.checkpoint(SPARK_CK_DIR)
    // kafka comsumer
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> KAFKA_BOOTSTRAP_SERVERS,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> KAFKA_COMSUMER_GROUPID,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val dstream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](KAFKA_COMSUMER_TOPICS, kafkaParams)
    )
    val startDate = dateFormat.get().parse(startDateStr)
    val endDate = dateFormat.get().parse(endDateStr)
    val startTime = timeFormat.get().parse(startTimeStr)
    val endTime = timeFormat.get().parse(endTimeStr)

    val df = ObjectSelectDriver.loadPersonAndPhone(condidtion)
    df.cache()
    import org.apache.spark.sql.functions._
    val attentionSfzhs = df.selectExpr("sfzh").rdd.map(row => row.getAs[String](0)).collect()
    val person2Info = df.groupBy("sfzh").agg(concat_ws(",", collect_set("xm")).alias("xm"),
      concat_ws(",", collect_set("archive_id")).alias("archive_id"),
      concat_ws(",", collect_set("tag_name")).alias("tag_name"),
      concat_ws(",", collect_set("classification")).alias("classification"),
      concat_ws(",", collect_set("pzhm")).alias("pzhm"),
      concat_ws(",", collect_set("phone")).alias("phone"),
      concat_ws(",", collect_set("organization_id")).alias("organization_id"),
      concat_ws(",", collect_set("org_name")).alias("org_name"),
      concat_ws(",", collect_set("zrr_id")).alias("zrr_id"),
      concat_ws(",", collect_set("zrr_name")).alias("zrr_name"))
      .rdd.map(row => {
      val j = new JSONObject()
      j.put("archive_id", row.getAs[String]("archive_id"))
      j.put("sfzh", row.getAs[String]("sfzh"))
      j.put("xm", row.getAs[String]("xm"))
      j.put("tag_name", row.getAs[String]("tag_name"))
      j.put("classification", row.getAs[String]("classification"))
      j.put("pzhm", row.getAs[String]("pzhm"))
      j.put("phone", row.getAs[String]("phone"))
      j.put("organization_id", row.getAs[String]("organization_id"))
      j.put("org_name", row.getAs[String]("org_name"))
      j.put("zrr_id", row.getAs[String]("zrr_id"))
      j.put("zrr_name", row.getAs[String]("zrr_name"))
      (row.getAs[String]("sfzh"), j)
    }).collectAsMap()


    // 过滤函数
    val flightOrderfilter = (record: JSONObject) => (FilterUntils.filterPerson(attentionSfzhs, record.getString("cecoS"))
      && FilterUntils.filterRegion(srcRegions, tgtRegions, record.getString("orai"), record.getString("deai"))
      && FilterUntils.filterDateTime(startDate, endDate, startTime, endTime, record.getString("stti"))
      && FilterUntils.filterRegionName(srcRegionNames, tgtRegionNames, record.getString("depa"), record.getString("dest")))


    // 初始化KafkaSink,并广播
    val kafkaProducer: Broadcast[KafkaSink[String, String]] = {
      val kafkaProducerConfig = {
        val p = new Properties()
        p.setProperty("bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS)
        p.setProperty("key.serializer", classOf[StringSerializer].getName)
        p.setProperty("value.serializer", classOf[StringSerializer].getName)
        p
      }
      streamingContext.sparkContext.broadcast(KafkaSink[String, String](kafkaProducerConfig))
    }


    dstream
      .map(record => JSON.parseObject(record.value()))
      .filter(flightOrderfilter)
      .foreachRDD(
        rdd => rdd.foreachPartition(
          it => it.foreach(v => {
            person2Info.getOrElse(v.getString("cecoS"), new JSONObject())
          }))
      )
    streamingContext
  }

}
