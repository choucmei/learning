package com.chouc.flink.atguigu.window

import com.chouc.flink.atguigu.Sensor
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Title: WindowCountDemo
 * @Package com.chouc.flink.atguigu.window
 * @Description:
 * @author chouc
 * @date 2021/5/24
 * @version V1.0
 */
class WindowCountDemo {

}

object WindowCountDemo {
  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala.createTypeInformation
    val source = environment.addSource(new SocketTextStreamFunction("localhost", 8888, "\n", 3))
    source.map(s => {
      val strings = s.split(",")
      Sensor(strings(0), strings(1).toLong, strings(2).toDouble)
    }).keyBy(s => s.id)
      .countWindow(4, 2)
      .aggregate(new AggregateFunction[Sensor, (Int, Double), (Int, Double, Double)] {
        override def createAccumulator(): (Int, Double) = (0, 0)

        override def add(value: Sensor, accumulator: (Int, Double)): (Int, Double) = {
          (accumulator._1 + 1, accumulator._2 + value.tmp)
        }

        override def getResult(accumulator: (Int, Double)): (Int, Double, Double) = {
          (accumulator._1, accumulator._2, accumulator._2 / accumulator._1)
        }

        override def merge(a: (Int, Double), b: (Int, Double)): (Int, Double) = {
          (a._1 + b._1, a._2 + b._2)
        }
      }).print()
    environment.execute("window count")
  }
}
