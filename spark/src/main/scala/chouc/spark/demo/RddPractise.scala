package chouc.spark.demo

import org.apache.spark.SparkConf

object RddPractise {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local[1]")
//    val sc = new SparkContext(conf)
//    val mbt = sc.textFile("/Users/chouc/Desktop/local_hadoop/spark/URL/itcast.log")
//    val b = mbt.map((_,1))
//    b = b.repartition()
    tmp.tm();
  }
}

object tmp{
  def tm(): Unit ={
    print("0")
  }
  print("1")
}