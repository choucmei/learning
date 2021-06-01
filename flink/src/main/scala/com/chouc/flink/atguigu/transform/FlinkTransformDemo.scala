package com.chouc.flink.atguigu.transform

import com.chouc.flink.atguigu.{ExampleData, Sensor}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Title: FlinkTransformDemo
 * @Package com.chouc.flink.atguigu.transform
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
object FlinkTransformDemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment;
    import org.apache.flink.streaming.api.scala.createTypeInformation

    /**
     *
     * max     only update col of max(col)
     * maxBy   update all cols
     *
     */
    env.fromCollection(ExampleData.getSensorData())
      .keyBy(s => s.id)
      .maxBy("tmp")
      .print()

    env.fromCollection(ExampleData.getSensorData())
      .keyBy(s => s.id)
      .reduce((s1: Sensor, s2: Sensor) => {
        Sensor(s1.id, s2.ts, s1.tmp.max(s2.tmp))
      }).print()

    env.execute()

  }
}
