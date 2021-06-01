package com.chouc.flink.atguigu.source

import java.util.Properties

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema


/**
 * @Title: FlinkFileSeqSource
 * @Package com.chouc.flink.atguigu.source
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
case class Sensor(id: String, times: Long, tmp: Double)

class UserDefineSource extends SourceFunction[Sensor] {
  var isStoped = false;

  override def run(ctx: SourceFunction.SourceContext[Sensor]): Unit = {
    while (!isStoped) {
      ctx.collect(Sensor("111", System.currentTimeMillis(), 9.1))
    }
  }

  override def cancel(): Unit = {
    isStoped = true;
  }
}

object FlinSourcesDemo {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.streaming.api.scala.createTypeInformation


    env.fromCollection(List(Sensor("1", 1111111L, 11.1), Sensor("1", 1111111L, 11.1), Sensor("1", 1111111L, 11.1))).print()

    env.fromElements(1111, 2222, "1111", 1.23).print()


    val properties = new Properties()
    properties.put("bootstrap.servers", "localhost:9092")
    env.addSource(new FlinkKafkaConsumer[String]("flink_learning", new SimpleStringSchema(), properties)).print()


    /** *
     * user define source
     */
    env.addSource(new UserDefineSource()).print()

    env.execute()

  }
}
