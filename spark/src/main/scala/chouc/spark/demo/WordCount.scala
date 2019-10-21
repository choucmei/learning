package chouc.spark.demo

import org.apache.spark.{SparkConf, SparkContext}


object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WC")
    //创建SparkContext，该对象是提交spark App的入口
    val sc = new SparkContext(conf)
    //使用sc创建RDD并执行相应的transformation和action
    val rdd = sc.parallelize(Seq("asd asdf fff aaa", "ffa fs aaa", "ffff xax"))
    //停止sc，结束该任务
    rdd.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).map(x=>(x._1,x._2.size))
    sc.stop()

  }
}
