package com.chouc.flink.atguigu.window

import java.text.SimpleDateFormat

import com.chouc.flink.atguigu.Sensor
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction
import org.apache.flink.streaming.api.scala.function.{RichWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.{TimeWindow, Window}
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerRecord

/**
 * @Title: WindowTimeDemo
 * @Package com.chouc.flink.atguigu.window
 * @Description:
 * @author chouc
 * @date 2021/5/25
 * @version V1.0
 */
class SensorAggregate extends AggregateFunction[Sensor, (Int, Double), (Int, Double, Double)] {
  override def createAccumulator(): (Int, Double) = (0, 0)

  override def add(value: Sensor, accumulator: (Int, Double)): (Int, Double) = (accumulator._1 + 1, accumulator._2 + value.tmp)

  override def getResult(accumulator: (Int, Double)): (Int, Double, Double) = (accumulator._1, accumulator._2, accumulator._2 / accumulator._1)

  override def merge(a: (Int, Double), b: (Int, Double)): (Int, Double) = (a._1 + b._1, a._2 + b._2)
}

class SensorWindowFunc extends RichWindowFunction[(Int, Double, Double), (String, String, String, Int, Double, Double), String, TimeWindow] {
  lazy private val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  override def apply(key: String, window: TimeWindow, input: Iterable[(Int, Double, Double)], out: Collector[(String, String, String, Int, Double, Double)]): Unit = {
    input.foreach(f => {
      println(s"${window.getStart} ${simpleDateFormat.format(window.getStart)}  => ${window.getEnd} ${simpleDateFormat.format(window.getEnd)}"+f)
      out.collect((key, simpleDateFormat.format(window.getStart), simpleDateFormat.format(window.getEnd), f._1, f._2, f._3))
    })
  }
}

object WindowTimeDemo {
  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    import org.apache.flink.streaming.api.scala.createTypeInformation
    val source = environment.addSource(new SocketTextStreamFunction("localhost", 8888, "\n", 3))
    val lateStream = new OutputTag[Sensor]("late") {}
    val sensorAggregate = source.map(s => {
      val strings = s.split(",")
      Sensor(strings(0), strings(1).toDouble, strings(2).toLong)
    }).assignTimestampsAndWatermarks(WatermarkStrategy.forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner[Sensor]() {
        override def extractTimestamp(element: Sensor, recordTimestamp: Long): Long = {
          element.ts
        }
      }))
      .keyBy(s => s.id)
      .timeWindow(Time.seconds(10), Time.seconds(5))
      .allowedLateness(Time.seconds(4))
      .sideOutputLateData(lateStream)
      .aggregate(new SensorAggregate(), new SensorWindowFunc())
    sensorAggregate.getSideOutput(lateStream).printToErr()
    sensorAggregate.print()

    environment.execute("demo")
  }
}