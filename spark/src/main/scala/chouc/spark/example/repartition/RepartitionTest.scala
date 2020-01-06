package chouc.spark.example.repartition

import org.apache.spark.sql.{Row, SparkSession}

/**
  * @Title: RepartitionTest
  * @Package chouc.spark.example.repartition
  * @Description:
  * @author chouc
  * @date 11/14/19
  * @version V1.0
  */
object RepartitionTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    import spark.implicits._
    val ds = spark.createDataset((1 to 10).toList)
    println("********************************** partitions " + ds.rdd.partitions.size)
    val nRdd = ds.rdd.coalesce(7, false)
    println("********************************** partitions " + nRdd.partitions.size)
    nRdd.foreachPartition(it => {
      println(s" ********************************** partition ${Thread.currentThread().getId}")
      it.foreach(println(_))
    })
  }
}
