package chouc.spark.sql

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
    val df = spark.read.parquet("/Users/chouc/Desktop/ad.db")


    df.select()

  }
}