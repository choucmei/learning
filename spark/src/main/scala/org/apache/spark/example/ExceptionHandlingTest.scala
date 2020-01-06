package org.apache.spark.example

import org.apache.spark.sql.SparkSession

/**
  * @Title: ExceptionHandlingTest
  * @Package org.apache.spark.example
  * @Description:
  * @author chouc
  * @date 10/23/19
  * @version V1.0
  */
object ExceptionHandlingTest {
  def main(args: Array[String]) {
    val spark = SparkSession
      .builder
      .appName("ExceptionHandlingTest")
      .getOrCreate()

    spark.sparkContext.parallelize(0 until spark.sparkContext.defaultParallelism).foreach { i =>
      if (math.random > 0.75) {
        throw new Exception("Testing exception handling")
      }
    }

    spark.stop()
  }
}
