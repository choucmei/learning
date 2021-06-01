package chouc.spark.alarm

import chouc.spark.alarm.FilterUntils.{dateFormat, timeFormat}
import org.apache.kafka.common.serialization.StringDeserializer
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
class CallAlarmDriverTest {

}

object CallAlarmDriverTest {
  def main(args: Array[String]): Unit = {
    val SPARK_CK_DIR = "file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName
    val ssc = creatingStreamingContextFunc(args)
    ssc.start()
    ssc.awaitTermination()
    sys.ShutdownHookThread {
      ssc.stop(stopSparkContext = true, stopGracefully = true)
    }
  }

  def creatingStreamingContextFunc(args: Array[String]): StreamingContext = {
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local[*]")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "1")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.setAppName("t")
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(2))
    streamingContext.sparkContext.setLogLevel("ERROR")

    val condition = "{\"type_name\":\"模型自定义人员\",\"control_type\":\"SZ\",\"control_level\":[],\"archive_sex\":[\"男\"],\"archive_age_range\":[],\"archive_household_Location\":\"\",\"archive_actual_location\":\"\",\"archive_idcard_number\":[\"\"]}"

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
    val KAFKA_BOOTSTRAP_SERVERS = ""
    val KAFKA_COMSUMER_TOPICS = "".split(",")
    val KAFKA_COMSUMER_SINK_TOPICS = ""
    val KAFKA_COMSUMER_GROUPID = this.getClass.getName
    val SPARK_CK_DIR = "file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName


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
    val dstream = streamingContext.socketTextStream("localhost", 9090)
    val startDate = dateFormat.get().parse(startDateStr)
    val endDate = dateFormat.get().parse(endDateStr)
    val startTime = timeFormat.get().parse(startTimeStr)
    val endTime = timeFormat.get().parse(endTimeStr)

    val df = ObjectSelectDriver.loadPersonAndPhone(condition)
    df.cache()
    val attentionPhones = df.selectExpr("phone").rdd.map(row => row.getAs[String](0)).collect()
    val phones2Person = df.selectExpr("phone", "xm", "sfzh").rdd
      .map(row => (row.getAs[String](0), (row.getAs[String](1), row.getAs[String](1)))).collect().toMap

    println(attentionPhones)
    println(phones2Person)

    // 过滤电话函数
    val phoneInfoFilter = (record: (String, String, String, String, String)) =>
      (FilterUntils.filterPhone(attentionPhones,
        callerPhones, calledPhones,
        callerAttributions, calledAttributions,
        record._1, record._2, record._3, record._4)
        && FilterUntils.filterDateTime(startDate, endDate, startTime, endTime, record._5))

    // 提取key函数
    val phoneInfo = (record: (String, String, String, String, String)) => {
      println(record)
      if (callerPhones.contains(record._1)) {
        (record._1, (1, 0))
      } else {
        (record._2, (0, 1))
      }
    }

    // 提取告警函数
    val alarmPhoneFileter = (data: (String, Int, Int)) => {
      data._2 >= callerTimes || data._3 >= calledTimes
    }


    val mappingFunc = (phone: String, cur: Option[(Int, Int)], state: State[(Int, Int)]) => {
      val curData = cur.getOrElse((0, 0))
      val stateData = state.getOption().getOrElse((0, 0))
      if (curData._1 == null) {
        (phone, stateData._1, stateData._2)
      } else {
        state.update((stateData._1 + curData._1, stateData._2 + curData._2))
        (phone, stateData._1 + curData._1, stateData._2 + curData._2)
      }
    }


    dstream.map(v => {
      val values = v.split(",")
      (values(0), values(1), values(2), values(3), values(4))
    }).filter(phoneInfoFilter)
      .map(phoneInfo)
      .mapWithState(StateSpec.function(mappingFunc))
      .filter(alarmPhoneFileter)
      .foreachRDD(
        rdd => rdd.foreachPartition(it => it.foreach(v => {
          println(phones2Person.getOrElse(v._1, ("", "")))
          //          kafkaProducer.value.send(KAFKA_COMSUMER_SINK_TOPICS, )
        }))
      )
    streamingContext
  }


}