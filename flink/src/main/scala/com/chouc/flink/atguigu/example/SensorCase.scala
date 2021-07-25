package com.chouc.flink.atguigu.example

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.KafkaConsumer

/**
 * @Title: SensorCase
 * @Package com.chouc.flink.`case`.sensor
 * @Description:
 * @author chouc
 * @date 2021/5/25
 * @version V1.0
 */
object SensorCase {
  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
//    import org.apache.flink.streaming.api.scala.createTypeInformation
    import org.apache.flink.streaming.api.scala._
    val properties = new Properties
    properties.setProperty("bootstrap.servers", "127.0.0.1:9092")
    val flinkKafkaConsumer = new FlinkKafkaConsumer[String]("flink_learning", new SimpleStringSchema, properties)
//    flinkKafkaConsumer.assignTimestampsAndWatermarks()
    val source = environment.addSource(flinkKafkaConsumer)

  }
}
