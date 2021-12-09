package chouc.spark.rdd

import org.apache.spark.sql.SparkSession

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

    nRdd.flatMap(v=>v.toString.split(" ").map((_,1))).reduceByKey(_+_).collect().map(println(_))
    println("********************************** partitions " + nRdd.partitions.size)
    nRdd.foreachPartition(it => {
      println(s" ********************************** partition ${Thread.currentThread().getId}")
      it.foreach(println(_))
    })
  }
}
