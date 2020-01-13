package chouc.spark.sql

import java.util.{Properties, UUID}

import org.apache.spark.sql.SparkSession

/**
  * @Title: SparkSql
  * @Package chouc.spark.sql
  * @Description:
  * @author chouc
  * @date 8/2/19
  * @version V1.0
  */
class SparkSql {

}

object SparkSql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()

    val mysqldf = spark.read.format("jdbc")
      .option("url","jdbc:mysql://slave01:3306/ad")
      .option("dbtable","exposure_click_bidding_1day")
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("password","starcor")
//      .option("partitionColumn","day")
//      .option("lowerBound","20180713")
//      .option("upperBound","20180718")
//      .option("numPartitions","5")
      .load()
//    mysqldf.foreachPartition(row=>{
//      println(UUID.randomUUID().toString)
//      row.foreach(v=>{
//        println("****")
//      })
//    })
    mysqldf.count()

//    spark.sql("show tables").show()
//
//    spark.sql("select count(1) from user_log where day=20190516 and minute=0445").show()

  }
}

case class Data(id: String, group: String, vlaue: Int)