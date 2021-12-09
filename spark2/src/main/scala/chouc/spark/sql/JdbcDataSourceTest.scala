package chouc.spark.sql

import org.apache.spark.sql.SparkSession

object JdbcDataSourceTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    normalTableSpecifyPartitions(spark)
//    normalTable(spark)
  }

  def partitionTable(spark:SparkSession): Unit ={
    val adDf = spark.read
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("url","jdbc:mysql://localhost:3306/wechat_article")
      .option("dbtable","exposure_click_bidding_1day")
      .format("jdbc")
      .load()
    println(s"**************************** partition Num ${adDf.rdd.partitions.size}")
    println(s"**************************** ${adDf.count()}")
  }


  def partitionTableSpecifyPartitions(spark:SparkSession): Unit ={
    val adDf = spark.read
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("url","jdbc:mysql://localhost:3306/wechat_article")
      .option("dbtable","exposure_click_bidding_1day")
      .option("partitionColumn","day")
      .option("lowerBound","20180713")
      .option("upperBound","20180718")
      .option("numPartitions","2")
      .format("jdbc")
      .load()
    println(s"**************************** partition Num ${adDf.rdd.partitions.size}")
    println(s"**************************** ${adDf.count()}")
  }

  def normalTable(spark:SparkSession): Unit ={
    val articleDf = spark.read
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("url","jdbc:mysql://localhost:3306/wechat_article")
//      .option("dbtable","`add`")
      .option("dbtable","article_info")
      .format("jdbc")
      .load()

    println(s"**************************** partition Num ${articleDf.rdd.partitions.size}")
    println(s"**************************** ${articleDf.count()}")
  }

  def normalTableSpecifyPartitions(spark:SparkSession): Unit ={
    val adDf = spark.read
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("url","jdbc:mysql://localhost:3306/wechat_article")
      .option("dbtable","`add`")
      .option("partitionColumn","day")
      .option("lowerBound","20191201")
      .option("upperBound","20191228")
      .option("numPartitions","2")
      .format("jdbc")
      .load()

    println(s"**************************** partition Num ${adDf.rdd.partitions.size}")
    println(s"**************************** ${adDf.count()}")
  }

}
