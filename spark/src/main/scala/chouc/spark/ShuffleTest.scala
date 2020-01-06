package chouc.spark

import org.apache.spark.sql.SparkSession

/**
  * @Title: ShuffleTest
  * @Package chouc.spark
  * @Description:
  * @author chouc
  * @date 12/26/19
  * @version V1.0
  */
object ShuffleTest {

  def main(args: Array[String]): Unit = {
    val spark  = SparkSession.builder().master("local[*]").getOrCreate();
    val rdd = spark.sparkContext.parallelize(Seq("mm asdf asdfas","asdf asdf fff","ffa ffa sss"))
//    rdd.flatMap(_.split(" ")).map((_,1)).reduceByKey()


  }

}
