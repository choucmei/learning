package chouc.spark.streaming

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.{Encoder, SparkSession}

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
    val spark = SparkSession.builder().master("local[*]").getOrCreate()  // 创建一个 SparkSession 程序入口
    import spark.implicits._
    val lines = spark.readStream.textFile("/Users/chouc/Work/IdeaProjects/learning/learning/spark/src/main/resources/example/")  // 将 some_dir 里的内容创建为 Dataset/DataFrame；即 input table
    val words = lines.as[String].flatMap(_.split(" "))
    val wordCounts = words.groupBy("value").count()    // 对 "value" 列做 count，得到多行二列的 Dataset/DataFrame；即 result table
    val query = wordCounts.writeStream                 // 打算写出 wordCounts 这个 Dataset/DataFrame
      .outputMode("complete")                          // 打算写出 wordCounts 的全量数据
      .format("console")                               // 打算写出到控制台
      .start()                                         // 新起一个线程开始真正不停写出

    query.awaitTermination()
  }
}
