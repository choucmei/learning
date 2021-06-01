package com.chouc.flink.atguigu.sink

import java.util.Properties

import org.apache.flink.core.fs.Path
import org.apache.flink.orc.OrcSplitReaderUtil
import org.apache.flink.orc.vector.RowDataVectorizer
import org.apache.flink.orc.writer.OrcBulkWriterFactory
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink.BulkFormatBuilder
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.data.{GenericRowData, RowData, StringData}
import org.apache.flink.table.types.logical._
import org.apache.hadoop.conf.Configuration

/**
 * @Title: FlinkSinkDemo
 * @Package com.chouc.flink.atguigu.sink
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
class FlinkSinkDemo {

}

object FlinkSinkDemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.enableCheckpointing(10000)

    val writerProps = new Properties();
    writerProps.setProperty("orc.compress", "LZ4");

    //定义类型和字段名
    val orcTypes = List(new VarCharType(), new BigIntType(), new DoubleType());
    val fields = List("id", "ts", "tmp")
    val b = RowType.of(orcTypes.toArray[LogicalType], fields.toArray[String])
    val typeDescription = OrcSplitReaderUtil.logicalTypeToOrcType(b)

    //构造工厂类OrcBulkWriterFactory
    val factory = new OrcBulkWriterFactory[RowData](new RowDataVectorizer(typeDescription.toString(), orcTypes.toArray), writerProps, new Configuration())
    val sink = StreamingFileSink.forBulkFormat(new Path("file:///Users/chouc/Desktop/flink_checkpoint/orc"), factory)
      .withBucketCheckInterval(1000*10)
      .build()

    import org.apache.flink.streaming.api.scala.createTypeInformation


    env.addSource(new SourceFunction[RowData] {
      override def run(ctx: SourceContext[RowData]): Unit = {
        var a = 1
        while (true) {
          val rowData = new GenericRowData(3)
          rowData.setField(0, StringData.fromString(String.valueOf(a)))
          rowData.setField(1, System.currentTimeMillis())
          rowData.setField(2, 1.2)
          ctx.collect(rowData);
          Thread.sleep(10);
          println(a)
          a += 1
        }
      }

      override def cancel(): Unit = {

      }

    }).rebalance.addSink(sink)
//    env.execute()
    println(env.getExecutionPlan)
  }
}