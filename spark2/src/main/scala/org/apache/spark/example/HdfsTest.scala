package org.apache.spark.example

import java.util.concurrent.TimeUnit

import org.apache.spark.sql.SparkSession

/**
  * @Title: HdfsTest
  * @Package org.apache.spark.example
  * @Description:
  * @author chouc
  * @date 10/23/19
  * @version V1.0
  */
object HdfsTest {

  /** Usage: HdfsTest [file] */
  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println("Usage: HdfsTest <file>")
      System.exit(1)
    }
    val spark = SparkSession
      .builder
      .appName("HdfsTest")
      .getOrCreate()
    val file = spark.read.text(args(0)).rdd
    val mapped = file.map(s => s.length).cache()
    for (iter <- 1 to 10) {
      val startTimeNs = System.nanoTime()
      for (x <- mapped) { x + 2 }
      val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTimeNs)
      println(s"Iteration $iter took $durationMs ms")
    }
    println(s"File contents: ${file.map(_.toString).take(1).mkString(",").slice(0, 10)}")
    println(s"Returned length(s) of: ${file.map(_.length).sum().toString}")
    spark.stop()
  }
}
