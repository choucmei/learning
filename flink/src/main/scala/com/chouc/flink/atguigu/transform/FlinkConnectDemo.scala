package com.chouc.flink.atguigu.transform

import com.chouc.flink.atguigu.ExampleData
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Title: FlinkConnectDemo
 * @Package com.chouc.flink.atguigu.transform
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
class FlinkConnectDemo {

}

object FlinkConnectDemo{
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala.createTypeInformation
    val ds1 = env.fromCollection(ExampleData.getSensorData())
    val ds2 = env.fromCollection(ExampleData.getSensorData())
    ds1.connect(ds2).map(v=>v,v=>v).print("connect")
    ds1.union(ds2).print("union")

    env.execute()
  }
}
