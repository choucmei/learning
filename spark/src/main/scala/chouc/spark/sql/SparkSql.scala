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


  }
}

case class Data(id: String, group: String, vlaue: Int)