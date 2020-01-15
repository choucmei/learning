package chouc.spark.rdd

import org.apache.spark.sql.SparkSession

object RddJoin {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").appName("join").getOrCreate()
    val rdd1 = spark.sparkContext.parallelize(List(("mxb",10),("mxb",10),("cjl",20),("zmt",30),("other","40")),3)
    val rdd2 = spark.sparkContext.parallelize(List(("mxb",10),("cjl",20),("zmt",30),("other","40"),("lack","50")),2)
    println(s"rdd1 ${rdd1.getNumPartitions}")
    println(s"rdd2 ${rdd2.getNumPartitions}")

    rdd1.map(_._1)

    val joinRdd = rdd1.leftOuterJoin(rdd2)

    val unionRdd = rdd1.union(rdd2)

    val subtractRDD = rdd1.subtract(rdd2)

    val intersectionRDD = rdd1.intersection(rdd2)

    val cogroupRdd = rdd1.cogroup(rdd2)

    rdd1.distinct()

    println(s"joinRdd ${joinRdd.getNumPartitions}")
    println(s"unionRdd ${unionRdd.getNumPartitions}")
    println(s"subtractRDD ${subtractRDD.getNumPartitions}")
    println(s"intersectionRDD ${intersectionRDD.getNumPartitions}")
    println(s"cogroupRdd ${cogroupRdd.getNumPartitions}")

    joinRdd.collect().foreach(println(_))
    unionRdd.collect().foreach(println(_))
    subtractRDD.collect().foreach(println(_))
    intersectionRDD.collect().foreach(println(_))
    cogroupRdd.collect().foreach(println(_))

  }
}
