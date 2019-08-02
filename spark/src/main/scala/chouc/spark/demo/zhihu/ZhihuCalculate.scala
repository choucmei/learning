package chouc.spark.demo.zhihu

import java.text.SimpleDateFormat
import java.util.Date

import kafka.serializer.StringDecoder
import net.sf.json.JSONObject
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Milliseconds, Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaManager
import org.apdplat.word.WordSegmenter

object ZhihuCalculate {

  def insert2Redis(key:String,word:String,num:Int): Unit ={
    JedisManager.process(key,num,word)
  }

  // val anw = JSONObject.fromObject(JSONObject.fromObject(json.get("Hot")).get("target"))
  def processAnswer(jsonString:String,answerType:Int): Unit ={
    var anw:JSONObject = null
    var anwType:String = null
    val topicMain = JSONObject.fromObject(jsonString).get("MaintopicId").toString
    val topicSub = JSONObject.fromObject(jsonString).get("SubTopicId").toString
    if (answerType==2){
      anw = JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(jsonString).get("Hot")).get("target"))
      anwType = "Hot"
    } else if (answerType==3){
      anw = JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(jsonString).get("Top")).get("target"))
      anwType = "Top"
    } else{
      anw = JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(jsonString).get("Un")).get("target"))
      anwType = "Un"
    }
    //获取时间戳
    val timestamp = anw.get("created").toString.toLong*1000
    //获取发布的时间
    val simpleDateFormat = new SimpleDateFormat("HH")
    val data = simpleDateFormat.format(new Date(timestamp))
    //存入redis
    insert2Redis("hourbang",data,1)
    insert2Redis("hourbangTopic",topicMain+":"+topicSub+":"+data,1)
    insert2Redis("hourbangType",anwType+":"+data,1)
    val title = anw.get("title").toString
    val wordList = WordSegmenter.seg(title)
    import scala.collection.JavaConverters._
    val list = wordList.asScala
    list.map(_.getText).map((_,1)).groupBy(_._1).mapValues(_.size).foreach(t=>{
      insert2Redis("wordsbang",t._1,t._2)
      insert2Redis("wordsbangTopic",topicMain+":"+t._1,t._2)
      insert2Redis("wordsbangType",anwType+":"+t._1,t._2)
    })
  }


  def processUser(userJson:String): Unit ={
//    JedisManager.jedis.zadd()
  }


  def process(rdd:RDD[(String, String)]){
    val lines = rdd.map(_._2)
    lines.foreach(line => {
      println(line)
      if (line.contains("INFO<1"))
        processUser(line)
      if (line.contains("INFO<2")){
        val jsonString = line.substring(line.indexOf("<")+2,line.lastIndexOf(">"))
        processAnswer(jsonString,2)
      }
      if ((line.contains("INFO<3"))){
        val jsonString = line.substring(line.indexOf("<")+2,line.lastIndexOf(">"))
        processAnswer(jsonString,3)
      }
      if (line.contains("INFO<4")){
        val jsonString = line.substring(line.indexOf("<")+2,line.lastIndexOf(">"))
        processAnswer(jsonString,4)
      }
    })
  }

  def processDb(count:Long): Unit ={
    println(count)
  }


  def main(args: Array[String]): Unit = {
    val params:Array[String] = Array("c3:9092,c4:9092,c5:9092","zhihu","zhi")
    if (params.length < 3) {
      System.err.println(
        s"""
           |Usage: DirectKafkaWordCount <brokers> <topics> <groupid>
           |  <brokers> is a list of one or more Kafka brokers
           |  <topics> is a list of one or more kafka topics to consume from
           |  <groupid> is a consume group
           |
        """.stripMargin)
      System.exit(1)
    }
    //获取本地参数
    val Array(brokers, topics, groupId) = params

    System.setProperty("HADOOP_USER_NAME", "root");
    val conf = new SparkConf().setAppName("ZhihuCalculate").setMaster("local[*]")
//    val conf = new SparkConf().setAppName("ZhihuCalculate").setJars(Array[String]("/Users/chouc/Work/IdeaProjects/Spark/target/Spark-1.0-SNAPSHOT-jar-with-dependencies.jar")).setMaster("spark://s1:7077")
    conf.set("spark.streaming.kafka.maxRatePerPartition", "2")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(conf, Milliseconds(500))

    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String,String](
      "metadata.broker.list"->brokers,
      "group.id" -> groupId,
      "auto.offset.reset" -> "smallest"
    )
    val km = new KafkaManager(kafkaParams = kafkaParams)

    val messages = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,topicSet)

    val result = messages.window(Seconds(1),Seconds(1)).map(a=>(1,1)).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.collect().foreach(t=>processDb(t._2))
    })


    println(result)
    messages.foreachRDD(rdd =>{
      if(!rdd.isEmpty())
        process(rdd)
        km.updateZKOffsets(rdd)
    })
    ssc.start()
    ssc.awaitTermination()

  }

}
