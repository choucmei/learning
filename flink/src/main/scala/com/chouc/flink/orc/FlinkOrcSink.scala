package com.chouc.flink.orc

import java.util.Properties

import org.apache.flink.core.fs.Path
import org.apache.flink.orc.writer.OrcBulkWriterFactory
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.hadoop.conf.Configuration
/**
 * @Title: FlinkOrcSink
 * @Package com.chouc.flink.orc
 * @Description:
 * @author chouc
 * @date 2021/2/7
 * @version V1.0
 */
class FlinkOrcSink {

}

object FlinkOrcSink {

  val schema: String ="struct<id:string,ts:bigint,temp:double>"
  val path:String = "/Users/chouc/Desktop/flink_checkpoint/orc/"

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala.createTypeInformation

    val writerProps = new Properties
    writerProps.setProperty("orc.compress", "LZ4")

    val factory = new OrcBulkWriterFactory[Sensor](new SensorVectorizer(FlinkOrcSink.schema), writerProps, new Configuration)

    env.setParallelism(1)
    env.enableCheckpointing(100)

    env.fromCollection(List(Sensor("1",1,1.2)))
      .addSink(StreamingFileSink.forBulkFormat(new Path(path), factory).build)
    env.execute()
  }
}
