package chouc.spark.datalake.iceberg

import org.apache.hudi.QuickstartUtils.{DataGenerator, convertToStringList}
import org.apache.spark.sql.SparkSession
import scala.collection.JavaConversions._

object SparkIcebergDemo {

  lazy val spark = SparkSession
    .builder()
    .config("spark.sql.catalog.hadoop_prod", "org.apache.iceberg.spark.SparkCatalog")
    .config("spark.sql.catalog.hadoop_prod.type","hadoop")
    .config("spark.sql.catalog.hadoop_prod.warehouse","hdfs:///warehouse/path")
    .master("local[*]")
    .getOrCreate()

  lazy val dataGen = new DataGenerator

  def main(args: Array[String]): Unit = {
    val inserts = convertToStringList(dataGen.generateInserts(10))
    val df = spark.read.json(spark.sparkContext.parallelize(inserts, 2))
    df.show()
    spark.sql("create database test_database").show()
    df.write
      .format("iceberg")
      .mode("append")
      .save("test_database.test_table")
  }

}
