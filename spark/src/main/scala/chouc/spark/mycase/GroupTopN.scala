package chouc.spark.mycase

import chouc.spark.sql.Data
import org.apache.spark.sql.SparkSession


object GroupTopN {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val rddLine = spark.sparkContext.parallelize(Seq("n1,g1,12", "n2,g1,13", "n3,g2,12", "n4,g2,19"))
    val rddData = rddLine.map(line => {
      val cols = line.split(",")
      Data(cols(0), cols(1), cols(2).toInt)
    })

    val result = rddData.groupBy(_.group).flatMap(kv => {
      kv._2.toSeq.sortBy(_.value).reverse.take(1)
    })

    result.collect().foreach(println(_))

  }
}


case class Data(name: String, group: String, value: Int)