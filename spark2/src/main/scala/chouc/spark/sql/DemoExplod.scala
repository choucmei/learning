package chouc.spark.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.functions._

/**
 * @Title: DemoExplod
 * @Package chouc.spark.sql
 * @Description:
 * @author chouc
 * @date 2021/2/2
 * @version V1.0
 */
class DemoExplod {

}

object DemoExplod {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").getOrCreate()
    spark.read.orc("file:///Users/chouc/Desktop/flink_checkpoint/orc/2021-02-07--16")
      .where("id='4'").show()
  }
}
