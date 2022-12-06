import org.apache.spark.sql.SparkSession

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
object SparkPi3 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Spark Pi")
            .master("local[*]")
      .config("spark.executor.instances", 2)
      .getOrCreate()
    val DBName = Array(
      Tuple2(1, "Spark"),
      Tuple2(2, "Hadoop"),
      Tuple2(3, "Kylin"),
      Tuple2(4, "Flink")
    )
    val numType = Array(
      Tuple2(1, "String"),
      Tuple2(2, "int"),
      Tuple2(3, "byte"),
      Tuple2(4, "bollean"),
      Tuple2(5, "float"),
      Tuple2(1, "34"),
      Tuple2(1, "45"),
      Tuple2(2, "47"),
      Tuple2(3, "75"),
      Tuple2(4, "95"),
      Tuple2(5, "16"),
      Tuple2(1, "85")
    )
    val names = spark.sparkContext.parallelize(DBName)
    val types = spark.sparkContext.parallelize(numType)

    val nameAndType = names.cogroup(types)

    //基于key进行join 结果并没有顺序
    nameAndType.collect.foreach(println)
  }
}
// scalastyle:on println
