package chouc.spark.sql

import org.apache.spark.sql.SparkSession

/**
  * @Title: SparkSql
  * @Package chouc.spark.sql
  * @Description:
  * @author chouc
  * @date 8/2/19
  * @version V1.0
  */
class SparkSql {

}

object SparkSql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("yarn-client").getOrCreate()
    import spark.implicits._
    val df  = spark.sparkContext.parallelize(Seq("1,123", "2,123", "3,123", "1,124"))
      .map(line => {
        val cols = line.split(",")
        Data(cols(0),cols(1))
      }).toDF
    df.show()

  }
}

case class Data(id: String, vlaue: String)