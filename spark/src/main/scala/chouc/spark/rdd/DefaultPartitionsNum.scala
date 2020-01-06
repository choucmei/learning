package chouc.spark.rdd

import java.util.UUID

import org.apache.spark.sql.{ForeachWriter, SparkSession}
import org.apache.spark.sql.streaming.DataStreamWriter
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
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    import spark.implicits._
    val rdda = spark.sparkContext.textFile("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/example/content").flatMap(_.split(" ")).map((_,1))
    val rddb = spark.sparkContext.textFile("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/example/content").flatMap(_.split(" ")).map((_,1))

    rdda.cartesian(rddb)


  }

}
