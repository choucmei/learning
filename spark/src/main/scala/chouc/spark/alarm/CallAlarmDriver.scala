package chouc.spark.alarm

import java.util.Properties

import chouc.spark.alarm.FilterUntils.{dateFormat, timeFormat}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
 * @Title: CallAlarmDriver
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/2/23
 * @version V1.0
 */
class CallAlarmDriver {

}

object CallAlarmDriver {
  def main(args: Array[String]): Unit = {
    val SPARK_CK_DIR = "file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName
    val condition = "{\"type_name\":\"模型自定义人员\",\"control_type\":\"SZ\",\"control_level\":[],\"archive_sex\":[\"男\"],\"archive_age_range\":[],\"archive_household_Location\":\"\",\"archive_actual_location\":\"\",\"archive_idcard_number\":[\"\"]}"
    //    val ssc = StreamingContext.getOrCreate(SPARK_CK_DIR, ()=>creatingStreamingContextFunc(args,SPARK_CK_DIR,condition))
    val ssc = creatingStreamingContextFunc(args, SPARK_CK_DIR, condition)
    ssc.start()
    ssc.awaitTermination()
    sys.ShutdownHookThread {
      ssc.stop(stopSparkContext = true, stopGracefully = true)
    }
  }

  def creatingStreamingContextFunc(args: Array[String], SPARK_CK_DIR: String, condidtion: String): StreamingContext = {
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(2))
//    sparkContext.addFile()
//    sparkContext.addFile()
    //告警参数
    val startDateStr = args(0)
    val endDateStr = args(1)
    val startTimeStr = args(2)
    val endTimeStr = args(3)
    val callerTimes = args(4).toInt
    val calledTimes = args(5).toInt
    val callerPhones = args(6).split(",")
    val calledPhones = args(7).split(",")
    val callerAttributions = args(8).split(",")
    val calledAttributions = args(9).split(",")

    // 配置参数
    val KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
    val KAFKA_COMSUMER_TOPICS = "test".split(",")
    val KAFKA_COMSUMER_SINK_TOPICS = "test"
    val KAFKA_COMSUMER_GROUPID = this.getClass.getName


    streamingContext.checkpoint(SPARK_CK_DIR)
    // kafka comsumer
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> KAFKA_BOOTSTRAP_SERVERS,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> KAFKA_COMSUMER_GROUPID,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
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
    val attentionPhones = df.selectExpr("phone").rdd.map(row => row.getAs[String](0)).collect()
    val phones2Sfzh = df.selectExpr("phone","sfzh").rdd
      .map(row => (row.getAs[String](0), row.getAs[String](1))).collect().toMap
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
      .rdd.map(row=>{
      val j = new JSONObject()
      j.put("archive_id",row.getAs[String]("archive_id"))
      j.put("sfzh",row.getAs[String]("sfzh"))
      j.put("xm",row.getAs[String]("xm"))
      j.put("tag_name",row.getAs[String]("tag_name"))
      j.put("classification",row.getAs[String]("classification"))
      j.put("pzhm",row.getAs[String]("pzhm"))
      j.put("phone",row.getAs[String]("phone"))
      j.put("organization_id",row.getAs[String]("organization_id"))
      j.put("org_name",row.getAs[String]("org_name"))
      j.put("zrr_id",row.getAs[String]("zrr_id"))
      j.put("zrr_name",row.getAs[String]("zrr_name"))
      (row.getAs[String]("sfzh"),j)
    }).collectAsMap()


    // 过滤电话函数
    val phoneInfoFilter = (record: JSONObject) =>
      (FilterUntils.filterPhone(attentionPhones,
        callerPhones, calledPhones,
        callerAttributions, calledAttributions,
        record.getString("caller"), record.getString("called"), record.getString("callerAttr"), record.getString("calledAttr"))
        && FilterUntils.filterDateTime(startDate, endDate, startTime, endTime, record.getString("dt")))

    // 提取key函数
    val phoneInfo = (record: JSONObject) => {
      println(record.toString)
      val caller = record.getString("caller")
      val called = record.getString("called")
      if (attentionPhones.contains(caller)) {
        (phones2Sfzh.getOrElse(caller,caller), (record, 1, 0))
      } else {
        (phones2Sfzh.getOrElse(called,called), (record, 0, 1))
      }
    }

    val mappingFunc = (sfzh: String, cur: Option[(JSONObject, Int, Int)], state: State[(Int, Int)]) => {
      val curData = cur.getOrElse((null, 0, 0))
      val stateData = state.getOption().getOrElse((0, 0))
      if (curData._1 == null) {
        (sfzh, stateData._1, stateData._2,"")
      } else {
        state.update((stateData._1 + curData._2, stateData._2 + curData._3))
        (sfzh, stateData._1 + curData._2, stateData._2 + curData._3, curData._1.toString)
      }
    }

    // 提取告警函数
    val alarmPhoneFileter = (data: (String, Int, Int,String)) => {
      data._2 >= callerTimes || data._3 >= calledTimes
    }

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
      .filter(phoneInfoFilter)
      .map(phoneInfo)
      .mapWithState(StateSpec.function(mappingFunc))
      .filter(alarmPhoneFileter)
      .foreachRDD(
        rdd => rdd.foreachPartition(it => {
          it.foreach(v => {
            val rs = new JSONObject(person2Info.getOrElse(v._1,new JSONObject()))
            rs.put("caller_times",v._2)
            rs.put("called_times",v._3)
            rs.put("orginal_data",v._4)
            println(rs.toString)
          })
        })
      )
    streamingContext
  }


}