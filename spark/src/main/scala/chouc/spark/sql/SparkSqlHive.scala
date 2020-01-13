package chouc.spark.sql

import org.apache.spark.sql.SparkSession

/**
  * @Title: SparkSqlHive
  * @Package chouc.spark.sql
  * @Description:
  * @author chouc
  * @date 1/8/20
  * @version V1.0
  */
object SparkSqlHive {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").enableHiveSupport().getOrCreate()
    spark.sql("select count(1) from user_log where day=20190516 and minute=2000").show()
  }
}
