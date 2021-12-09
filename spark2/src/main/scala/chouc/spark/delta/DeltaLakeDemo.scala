package chouc.spark.delta

import org.apache.spark.sql.SparkSession

/**
 * @Title: DeltaLakeDemo
 * @Package chouc.spark.delta
 * @Description:
 * @author chouc
 * @date 6/30/20
 * @version V1.0
 */
class DeltaLakeDemo {

}

object DeltaLakeDemo {

  var spark:SparkSession = null

  def main(args: Array[String]): Unit = {
    spark = SparkSession
      .builder()
      .master("local[*]")
      .config("spark.sql.warehouse.dir","/Users/chouc/Desktop/warehouse")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .getOrCreate()
    demo02()
  }

  def demo01(): Unit ={
    val data = spark.range(0, 5)
    data.write.format("delta").save("/Users/chouc/Desktop/warehouse/delta/delta-table")
  }

  def demo02(): Unit ={
    val sql =
      """
        |CREATE TABLE events (
        | date DATE,
        | eventId STRING,
        | eventType STRING,
        | data STRING)
        |USING DELTA
        |PARTITIONED BY (date)
        |LOCATION '/Users/chouc/Desktop/warehouse/delta/events'
        |""".stripMargin
    spark.sql("show tables ").show()
    spark.sql(sql).dropDuplicates()
    spark.sql("show tables ").show()
  }

}