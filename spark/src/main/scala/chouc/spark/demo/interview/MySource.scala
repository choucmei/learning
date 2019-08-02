package chouc.spark.demo.interview

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object MySource {

  val data2MySQL = (iterator: Iterator[(String, Int,Int)]) => {
    var conn: Connection = null
    var ps : PreparedStatement = null
    val sql = "INSERT INTO good_student_infos (name, age, score) VALUES (?, ?, ?)"
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "123456")
      iterator.foreach(line => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, line._1)
        ps.setInt(2, line._2)
        ps.setInt(3, line._3)
        ps.executeUpdate()
      })
    } catch {
      case e: Exception => println("Mysql Exception")
    } finally {
      if (ps != null)
        ps.close()
      if (conn != null)
        conn.close()
    }
  }


  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("JdbcRDDDemo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val connection = () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "123456")
    }
    // placeholders for parameters used to partition the results.  but i define the num of partitions is 1 .
    val studentinfo = new JdbcRDD(
      sc,
      connection,
      "SELECT * FROM student_infos where age >= ? and age <= ?",
      0, 100, 1,
      r => {
        val name = r.getString(1)
        val age = r.getInt(2)
        (name, age)
      })
    // placeholders for parameters used to partition the results.  but i define the num of partitions is 1 .
    val student_score = new JdbcRDD(
      sc,
      connection,
      "SELECT * FROM student_scores where name >= ? and name <= ?",
      0, 100, 1,
      r => {
        val name = r.getString(1)
        val score = r.getInt(2)
        (name, score)
      })
    val good_stu = studentinfo.join(student_score).filter(_._2._2>80).map(t =>{
      (t._1,t._2._1,t._2._2)
    })
    good_stu.toJavaRDD().rdd
    good_stu.foreachPartition(data2MySQL(_))
    print(good_stu.collect().toBuffer)
  }
}
