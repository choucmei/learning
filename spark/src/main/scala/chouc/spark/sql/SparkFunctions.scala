package chouc.spark.sql

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.junit.{Before, Test}

class SparkFunctions {

  var spark: SparkSession = null
  var df: DataFrame = null



  @Before
  def befor(): Unit = {
    spark = SparkSession.builder().master("local[*]").getOrCreate()
    val rddLine = spark.sparkContext.parallelize(Seq("n1,g1,12", "n2,g1,13", "n3,g2,12", "n4,g2,19", "n5,g2,19"))
    val sparkSession = spark
    import sparkSession.implicits._
    df = rddLine.map(line => {
      val cols = line.split(",")
      Data(cols(0), cols(1), cols(2).toInt)
    }).toDF()
    df.cache()
  }

  @Test
  def windowFunction(): Unit = {
    val w = Window.partitionBy("group").orderBy(desc("value"))
    df.withColumn("rank", rank.over(w)).where("row > 0").show()

    df.withColumn("row", row_number.over(w)).where("row > 0").show()

    //    df.select(col("name"))

    //                rank                     row_number
    //      +----+-----+-----+----+          +----+-----+-----+---+
    //      |name|group|value|rank|          |name|group|value|row|
    //      +----+-----+-----+----+          +----+-----+-----+---+
    //      |  n4|   g2|   19|   1|          |  n4|   g2|   19|  1|
    //      |  n5|   g2|   19|   1|          |  n5|   g2|   19|  2|
    //      |  n3|   g2|   12|   3|          |  n3|   g2|   12|  3|
    //      |  n2|   g1|   13|   1|          |  n2|   g1|   13|  1|
    //      |  n1|   g1|   12|   2|          |  n1|   g1|   12|  2|
    //      +----+-----+-----+----+          +----+-----+-----+---+
  }


  @Test
  def sqlWindowFunction(): Unit = {
    df.createOrReplaceTempView("tmp")
    spark.sql("select * from (select name,group,value,rank() over(partition by group order by value desc) rank from tmp) where rank <= 1").show()
  }


  @Test
  def udfTest(): Unit = {
    spark.udf.register("multiply", (p1: Int, p2: Int) => {
      p1 * p2
    })
    df.selectExpr("multiply(value,2)").show()
  }

  @Test
  def udfTest01(): Unit = {
    val multiply = (p1: Int, p2: Int) => {
      p1 * p2
    }
    val multiplyUDF = udf(multiply)
    df.select(multiplyUDF(col("value"), lit(2)),col("name")).show()
    df.withColumn("result", multiplyUDF(col("value"), lit(1))).show()
  }


  @Test
  def udafTest(): Unit = {
    val udaf = new AverageUserDefinedAggregateFunction
//    val udf = udf(udaf)
    spark.udf.register("u_avg",new AverageUserDefinedAggregateFunction)
    df.groupBy(col("group")).agg(Map("value"->"u_avg")).show()

//    df.where($"asd"===1)

  }


}


case class Data(name: String, group: String, value: Int)