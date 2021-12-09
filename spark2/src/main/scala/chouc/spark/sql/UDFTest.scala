package chouc.spark.sql

import org.apache.spark.sql.SparkSession

object UDFTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val wechatArticleDF = spark.read.option("url", "jdbc:mysql://localhost:3306/wechat_article")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "exposure_9click_bidding_1day")
      .option("user", "root")
      .format("jdbc")
      .load()
//    wechatArticleDF.join()
    spark.udf register("strLength", (p1:String,p2: String) => {
      p1.length + p2.length
    })
    wechatArticleDF.show()
    wechatArticleDF.selectExpr("strLength(advertiserid,creativeid)","strLength(advertiserid,creativeid)").show()
  }
}
