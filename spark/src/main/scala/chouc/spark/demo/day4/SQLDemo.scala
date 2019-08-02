package chouc.spark.demo.day4

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object SQLDemo {

  def main(args: Array[String]) {
    val sparkSession = SparkSession.builder().getOrCreate()
    System.setProperty("user.name","sparksql")

    val personRdd = sparkSession.sparkContext.textFile("hdfs://node-1.itcast.cn:9000/person.txt").map(line => {
      val fields = line.split(",")
      Person(fields(0).toLong, fields(1), fields(2).toInt)
    })

    import sparkSession.implicits._
    val personDf = personRdd.toDF
    personDf.createOrReplaceGlobalTempView("person")
    sparkSession.sql("select * from person where age >= 20 order by age desc limit 2").show()

  }
}

case class Person(id: Long, name: String, age: Int)
