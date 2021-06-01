package com.chouc.flink.atguigu.source

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.orc.{OrcRowInputFormat, OrcSplitReaderUtil}
import org.apache.flink.table.types.logical.{BigIntType, DoubleType, LogicalType, RowType, VarCharType}
import org.apache.hadoop.conf.Configuration

/**
 * @Title: FlinkOrcFileSourceDemo
 * @Package com.chouc.flink.atguigu.source
 * @Description:
 * @author chouc
 * @date 2021/2/7
 * @version V1.0
 */
class FlinkOrcFileSourceDemo {

}

object FlinkOrcFileSourceDemo {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala.createTypeInformation
    //定义类型和字段名
    val orcTypes = List(new VarCharType(), new BigIntType(), new DoubleType());
    val fields = List("id", "ts", "tmp")
    val b = RowType.of(orcTypes.toArray[LogicalType], fields.toArray[String])
    val typeDescription = OrcSplitReaderUtil.logicalTypeToOrcType(b)
    env.readFile(new OrcRowInputFormat("file:///Users/chouc/Desktop/flink_checkpoint/orc/2021-02-07--16", typeDescription, new Configuration(), 1000), "file:///Users/chouc/Desktop/flink_checkpoint/orc/2021-02-07--16")
      .print("*****")
    env.execute()
  }
}