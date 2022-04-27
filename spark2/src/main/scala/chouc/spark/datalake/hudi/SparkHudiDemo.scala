package chouc.spark.datalake.hudi


import org.apache.hudi.DataSourceReadOptions
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.QuickstartUtils._
import org.apache.hudi.common.model.OverwriteWithLatestAvroPayload
import org.apache.hudi.config.HoodieWriteConfig
import org.apache.spark.sql.SaveMode._
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._


object SparkHudiDemo {

  lazy val spark = SparkSession
    .builder()
    .master("local[*]")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  lazy val basePath = "hdfs:///tmp/hudi_trips_cow"
  lazy val tableName = "hudi_trips_cow"
  lazy val dataGen = new DataGenerator

  def main(args: Array[String]): Unit = {
    println(getQuickstartWriteConfigs)
    println(DataSourceWriteOptions.PRECOMBINE_FIELD.key())
    println(DataSourceWriteOptions.RECORDKEY_FIELD.key())
    println(DataSourceWriteOptions.PARTITIONPATH_FIELD.key())
    println(DataSourceWriteOptions.TABLE_NAME.key())


    println(HoodieWriteConfig.PRECOMBINE_FIELD_NAME.key())
    println(HoodieWriteConfig.TBL_NAME.key())
    println(HoodieWriteConfig.TBL_NAME.key())



    dataGen.generateInserts(10).map(hoodieRecord => {
      val data = hoodieRecord.getData.asInstanceOf[OverwriteWithLatestAvroPayload]
      println(data.getMetadata)
      println(data)
    })

     read()
  }

  // insert
  def insert(): Unit = {
    val inserts = convertToStringList(dataGen.generateInserts(10))
    val df = spark.read.json(spark.sparkContext.parallelize(inserts, 2))
    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(DataSourceWriteOptions.PRECOMBINE_FIELD.key(), "ts").
      option(DataSourceWriteOptions.RECORDKEY_FIELD.key(), "uuid").
      option(DataSourceWriteOptions.PARTITIONPATH_FIELD.key(), "partitionpath").
      option(HoodieWriteConfig.TBL_NAME.key(), tableName).
      mode(Overwrite).
      save(basePath)
  }

  // query
  def read(): Unit = {
    spark.read.
      format("hudi").
      load(basePath).
      show(100, false)
  }

  // update
  def update(): Unit = {
    val updates = convertToStringList(dataGen.generateUpdates(10))
    val df = spark.read.json(spark.sparkContext.parallelize(updates, 2))
    df.write.format("hudi").
      options(getQuickstartWriteConfigs).
      option(DataSourceWriteOptions.PRECOMBINE_FIELD.key(), "ts").
      option(DataSourceWriteOptions.RECORDKEY_FIELD.key(), "uuid").
      option(DataSourceWriteOptions.PARTITIONPATH_FIELD.key(), "partitionpath").
      option(HoodieWriteConfig.TBL_NAME.key(), tableName).
      mode(Append).
      save(basePath)
  }

  // incremental query
  def incrementalQuery(): Unit = {
    spark.
      read.
      format("hudi").
      load(basePath).
      createOrReplaceTempView("hudi_trips_snapshot")

    val commits = spark.sql("select distinct(_hoodie_commit_time) as commitTime from hudi_trips_snapshot order by commitTime").rdd.map(k => k.getString(0)).take(50)
    commits.map(print(_))
    val beginTime = commits(commits.length - 2) // commit time we are interested in

    // incrementally query data
    val tripsIncrementalDF = spark.read.format("hudi").
      option(DataSourceReadOptions.QUERY_TYPE.key(), DataSourceReadOptions.QUERY_TYPE_INCREMENTAL_OPT_VAL).
      option(DataSourceReadOptions.BEGIN_INSTANTTIME.key(), beginTime).
      load(basePath)
    tripsIncrementalDF.createOrReplaceTempView("hudi_trips_incremental")

    spark.sql("select `_hoodie_commit_time`, fare, begin_lon, begin_lat, ts from  hudi_trips_incremental where fare > 20.0").show()
  }


}
