package chouc.spark.mycase

import chouc.spark.sql.Data
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{row_number, desc}

object GroupTopNDataFrame {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val rddLine = spark.sparkContext.parallelize(Seq("n1,g1,12", "n2,g1,13", "n3,g2,12", "n4,g2,19"))
    import spark.implicits._
    val df = rddLine.map(line => {
      val cols = line.split(",")
      Data(cols(0), cols(1), cols(2).toInt)
    }).toDF()
    val w = Window.partitionBy($"group").orderBy(desc("value"))
    df.withColumn("rank", row_number.over(w)).where($"rank" <= 1).show()

    df.createOrReplaceTempView("tmp")
    spark.sql("select name,group,value,row_number() over(partition by group order by value) from tmp").show()
  }
}