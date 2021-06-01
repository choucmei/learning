package com.chouc.flink.atguigu.transform

import com.chouc.flink.atguigu.ExampleData
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Title: FlinkSplitDemo
 * @Package com.chouc.flink.atguigu.transform
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
class FlinkSplitDemo {

}

object FlinkSplitDemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala.createTypeInformation
    val splitStream = env.fromCollection(ExampleData.getSensorData())
      .split(s => if (s.tmp > 9.2) Seq("high") else Seq("low"))
    splitStream.select("high").print("high")
    splitStream.select("low").print("low")
    splitStream.select("high", "low").print("all")
    env.execute()
  }
}