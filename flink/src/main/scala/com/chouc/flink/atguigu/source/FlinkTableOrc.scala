package com.chouc.flink.atguigu.source

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.orc.OrcTableSource
import org.apache.flink.table.api.bridge.scala.BatchTableEnvironment
import org.apache.flink.table.api.internal.TableEnvironmentInternal

/**
 * @Title: FlinkTableOrc
 * @Package com.chouc.flink.atguigu.source
 * @Description:
 * @author chouc
 * @date 2021/2/7
 * @version V1.0
 */
object FlinkTableOrc {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tEnv = BatchTableEnvironment.create(env)
    val schema = "struct<id:string,ts:bigint,tmp:double>";
    val orc = OrcTableSource.builder.path("file:///Users/chouc/Desktop/flink_checkpoint/orc/2021-02-07--16").forOrcSchema(schema).build
    tEnv.asInstanceOf[TableEnvironmentInternal].registerTableSourceInternal("OrcTable", orc)
    val query = "select * from OrcTable"
    tEnv.sqlQuery(query).execute().print()
  }
}
