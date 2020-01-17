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
    System.setProperty("hadoop.home.dir","D:\\Development\\hadoop-2.9.2")
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local[*]").setAppName("ms")
    val sparkContext = SparkContext.getOrCreate(sparkConf)
    sparkContext.setCheckpointDir("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\example\\")
    val rdd1 = sparkContext.textFile("D:\\Development\\IdeaProjects\\chouc\\learning\\spark\\src\\main\\resources\\example\\content")
    println(rdd1.getNumPartitions)
    val rdd2 = rdd1.flatMap(_.split(" ")).map((_, 1))

    sparkContext.broadcast()

    rdd2.checkpoint()
    rdd2.foreach(println(_))
  }

}
