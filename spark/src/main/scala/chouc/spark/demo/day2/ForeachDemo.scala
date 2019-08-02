package chouc.spark.demo.day2

import org.apache.spark.{SparkConf, SparkContext}

object ForeachDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local")
    val sc = new SparkContext(conf)
    //
    //

    sc.stop()

  }
}
