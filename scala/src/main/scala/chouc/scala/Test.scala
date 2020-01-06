package chouc.scala

import ru.yandex.clickhouse.BalancedClickhouseDataSource

/**
  * @Title: Test
  * @Package chouc.scala
  * @Description:
  * @author chouc
  * @date 11/21/19
  * @version V1.0
  */
object Test {

  def main(args: Array[String]): Unit = {
    val con = new BalancedClickhouseDataSource("jdbc:clickhouse://slave01:8123");
    val datasource = con.getConnection
    val statement = datasource.createStatement()
    statement.addBatch("insert into test.test values ('2')")
    statement.addBatch("insert into test.test values ('2')")
    statement.addBatch("insert into test.test values ('2')")
    statement.addBatch("insert into test.test values ('2')")
    statement.addBatch("insert into test.test values ('2')")
    statement.executeLargeBatch()
    val a = statement.executeBatch()
    println(String.valueOf())
  }
}
