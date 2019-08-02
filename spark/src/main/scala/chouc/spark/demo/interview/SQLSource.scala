package chouc.spark.demo.interview

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object SQLSource {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("dfsource").setMaster("local[2]")
    val sqlContext = SparkSession.builder().appName("aa").config(sparkConf).getOrCreate()
    val properties = new Properties()
    properties.put("user","root")
    properties.put("password","123456")
    val url = "jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull"
    val student_infoDF = sqlContext.read.jdbc(url,"student_infos",properties)
    val student_scoreDF = sqlContext.read.jdbc(url,"student_scores",properties)
    val good_student = student_infoDF.join(student_scoreDF,"name").filter("score > 80")
    good_student.write.mode(SaveMode.Append).jdbc(url,"good_student_infos",properties)
    //    val stu = stud_scoreDF.rdd
//    print(stu.collect().toBuffer)
  }

}
