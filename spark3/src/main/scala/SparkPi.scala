import org.apache.spark.sql.SparkSession
import scala.math.random
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println

/** Computes an approximation to pi */
object SparkPi {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Spark Pi")
      .master("local[*]")
      .master("spark://192.168.3.107:7077")
      .getOrCreate()
    spark.read.parquet("hdfs:///user/hive/warehouse/orders_copy").show()
//    spark.read.parquet("hdfs:///user/hive/warehouse/orders_copy")
//      .selectExpr("order_id", "user_id")
//      .write.save("file:///tmp/d/out1/")
//    spark.read.parquet("hdfs:///user/hive/warehouse/orders_copy")
//      .selectExpr("order_id", "price")
//      .write.save("file:///tmp/d/out2/")


//    println(spark.read.text("file:///home/chouc/dev/md/Presto.md").count())
    Thread.sleep( 1000 * 60 * 20)
    spark.stop()
  }
}
// scalastyle:on println
