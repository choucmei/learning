package chouc.spark.datalake.hudi


import chouc.spark.delta.DeltaLakeDemo.spark
import org.apache.hudi.QuickstartUtils._

import scala.collection.JavaConversions._
import org.apache.spark.sql.SaveMode._
import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.config.HoodieWriteConfig._
import org.apache.spark.sql.SparkSession


object SparkHudiDemo {

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  def main(args: Array[String]): Unit = {

    val tableName = "hudi_trips_cow"
    val basePath = "hdfs:///tmp/hudi_trips_cow"
    val dataGen = new DataGenerator
    val inserts = convertToStringList(dataGen.generateInserts(10))
    val df = spark.read.json(spark.sparkContext.parallelize(inserts, 2))
    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(PRECOMBINE_FIELD.key(), "ts").
      option(RECORDKEY_FIELD.key(), "uuid").
      option(PARTITIONPATH_FIELD.key(), "partitionpath").
      option(TBL_NAME.key(), tableName).
      mode(Overwrite).
      save(basePath)
  }
}
