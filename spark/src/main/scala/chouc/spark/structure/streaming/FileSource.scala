package chouc.spark.structure.streaming

import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.types._

/**
  * @Title: FileSource
  * @Package chouc.spark.structure.streaming
  * @Description:
  * @author chouc
  * @date 8/28/19
  * @version V1.0
  */
object FileSource {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
  }
}
