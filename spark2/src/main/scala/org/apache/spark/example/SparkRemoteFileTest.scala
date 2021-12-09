package org.apache.spark.example

import java.io.File

import org.apache.spark.SparkFiles
import org.apache.spark.sql.SparkSession

/** Usage: SparkRemoteFileTest [file] */
object SparkRemoteFileTest {
  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println("Usage: SparkRemoteFileTest <file>")
      System.exit(1)
    }
    val spark = SparkSession
      .builder()
      .appName("SparkRemoteFileTest")
      .getOrCreate()
    val sc = spark.sparkContext
    val rdd = sc.parallelize(Seq(1)).map(_ => {
      val localLocation = SparkFiles.get(args(0))
      println(s"${args(0)} is stored at: $localLocation")
      new File(localLocation).isFile
    })
    val truthCheck = rdd.collect().head
    println(s"Mounting of ${args(0)} was $truthCheck")
    spark.stop()
  }
}
