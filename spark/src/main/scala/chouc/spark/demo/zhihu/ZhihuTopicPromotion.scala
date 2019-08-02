package chouc.spark.demo.zhihu

import net.sf.json.JSONObject
import org.apache.spark.{SparkConf, SparkContext}

object ZhihuTopicPromotion {

  def processUser(line:String): (String,String) ={
    val jsonString = line.substring(line.indexOf("<")+2,line.lastIndexOf(">"))
    val all = JSONObject.fromObject(jsonString)
    val subTopic = all.get("SubTopicId").toString
    val userId = JSONObject.fromObject(all.get("User")).get("id").toString
    (userId,subTopic)
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local[*]")
    //val conf = new SparkConf().setAppName("ZhihuCalculate").setJars(Array[String]("/Users/chouc/Work/IdeaProjects/Spark/target/Spark-1.0-SNAPSHOT-jar-with-dependencies.jar")).setMaster("spark://s1:7077")
    val sc = new SparkContext(conf)
    val mbt = sc.textFile("/Users/chouc/Desktop/zhihudata/Zhihu.log.2018-03-26")
    mbt.checkpoint()

    val nrdd1 = mbt.filter(_.contains("INFO<1")).map(line=>{
      val t = processUser(line)
      (t._1,t._2)
    }).groupByKey()
    val nrdd2 = nrdd1.map(t=>{
      val idList = t._2.toList.distinct.sorted.combinations(2).map(l=>{
        val id = l(0).toString+":"+l(1).toString
        (id,1)
      })
      idList.toMap
    }).flatMap(x=>x).reduceByKey(_+_)
    val t = nrdd2.sortBy(_._2,false).take(10)
    println(t.toList)
  }

}
