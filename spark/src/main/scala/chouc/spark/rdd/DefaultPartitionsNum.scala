package chouc.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Title: DefaultPartitionsNum
  * @Package chouc.spark.rdd
  * @Description:
  * @author chouc
  * @date 8/28/19
  * @version V1.0
  */
object DefaultPartitionsNum {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local[*]")
    val sparkContext = SparkContext.getOrCreate(sparkConf);
    val rdd1 = sparkContext.textFile("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/example/content")
    println(rdd1.getNumPartitions)
    val rdd2 = rdd1.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_)


  }

}
