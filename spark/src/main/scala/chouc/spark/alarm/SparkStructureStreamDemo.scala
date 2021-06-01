package chouc.spark.alarm

import org.apache.spark.SparkConf
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
 * @Title: SparkStateTest
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/2/23
 * @version V1.0
 */
class SparkStructureStreamDemo {

}

object SparkStructureStreamDemo {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().master("local[*]").getOrCreate()
    val dataframe = sparkSession.readStream.format("socket")
                            .option("host","localhost")
                            .option("port","9090")
                            .load()
    import sparkSession.sqlContext._
//    dataframe.flatMap(row=>row.getAs[String](0).split(" ").map((_,1)))
//    val streamQuery = dataframe.writeStream.foreach(new ForeachWriter[Row]() {
//      override def open(partitionId: Long, version: Long): Boolean = {
//        true
//      }
//
//      override def process(value: Row): Unit = {
//        println(value.getAs[String](0))
//      }
//
//      override def close(errorOrNull: Throwable): Unit = {
//
//      }
//    }).option("checkpointLocation","file:///Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/checkpoint/" + this.getClass.getName).start()
//
//
//    streamQuery.awaitTermination()
  }

}
