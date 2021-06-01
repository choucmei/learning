package com.chouc.flink.orc

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.orc.OrcTableSource
import org.apache.flink.table.api.bridge.scala.BatchTableEnvironment
import org.apache.flink.table.api.internal.TableEnvironmentInternal

/**
 * @Title: FlinkTableOrcDemo
 * @Package com.chouc.flink.orc
 * @Description:
 * @author chouc
 * @date 2021/2/7
 * @version V1.0
 */
class FlinkTableOrcDemo {

}

object FlinkTableOrcDemo {
  def main(args: Array[String]): Unit = {
    val schema: String ="struct<id:string,ts:bigint,tmp:double>"
    val path:String = "/Users/chouc/Desktop/flink_checkpoint/orc/2021-02-07--13"


    val env = ExecutionEnvironment.getExecutionEnvironment
    val table = BatchTableEnvironment.create(env)

    val orc = OrcTableSource.builder.path(FlinkOrcSink.path).forOrcSchema(schema).build
    table.asInstanceOf[TableEnvironmentInternal].registerTableSourceInternal("OrcTable", orc)

    val sql = """select * from OrcTable"""
    table.sqlQuery(sql).execute().print()

  }
}
