package hudi

import org.apache.spark.sql.SparkSession

object SparkReadCOW {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    readCOW(spark)
  }

  def readCOW(spark: SparkSession): Unit = {
    // spark-shell
    val tripsSnapshotDF = spark.
      read.
      format("hudi").
      load("file:///home/chouc/dev/WorkSpace/choucmei/learning/spark3/src/main/resources/stock_ticks_cow")
    tripsSnapshotDF.createOrReplaceTempView("hudi_trips_snapshot")
    spark.sql("select * from  hudi_trips_snapshot ").show()
  }

}
