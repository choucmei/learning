package com.chouc.flink.atguigu.process

import com.chouc.flink.atguigu.Sensor
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.state.{StateTtlConfig, ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.time.Time
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerRecord

object ProcessFunctionDemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._
    val source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3))
    source.flatMap((line, collector: Collector[Sensor]) => {
      val fields = line.split(",")
      collector.collect(Sensor(fields(0), fields(1).toDouble, fields(2).toLong * 1000))
    }).assignTimestampsAndWatermarks(WatermarkStrategy.forMonotonousTimestamps[Sensor].withTimestampAssigner(new SerializableTimestampAssigner[Sensor]() {
      override def extractTimestamp(element: Sensor, recordTimestamp: Long): Long = { // 提取事件事件
        element.ts
      }
    })).keyBy(_.id)
      .process(new TempWarningProcess(10 * 1000))
      .print()

    env.execute()
  }
}

class TempWarningProcess(interval: Long) extends KeyedProcessFunction[String, Sensor, String] {
  //  val stateTtlConfig = StateTtlConfig.newBuilder(Time.seconds(interval / 1000)).build()
  lazy val tempValueStateDescriptor: ValueStateDescriptor[Double] = new ValueStateDescriptor[Double]("temp", classOf[Double])
  //  tempValueStateDescriptor.enableTimeToLive(stateTtlConfig)
  lazy val tempValueState: ValueState[Double] = getRuntimeContext.getState(tempValueStateDescriptor)

  lazy val fireTimeValueStateDescriptor: ValueStateDescriptor[Long] = new ValueStateDescriptor[Long]("time", classOf[Long])
  //  fireTimeValueStateDescriptor.enableTimeToLive(stateTtlConfig)
  lazy val fireTimeValueState: ValueState[Long] = getRuntimeContext.getState(fireTimeValueStateDescriptor)

  override def processElement(value: Sensor, ctx: KeyedProcessFunction[String, Sensor, String]#Context, out: Collector[String]): Unit = {

    val lastTemp = tempValueState.value()
    tempValueState.update(value.tmp)
    val freeTimestamp = fireTimeValueState.value()
    println(lastTemp)
    println(freeTimestamp)
    if (value.tmp > lastTemp && freeTimestamp == 0) {
      val fireTime = ctx.timerService().currentProcessingTime() + interval
      fireTimeValueState.update(fireTime)
      ctx.timerService().registerProcessingTimeTimer(fireTime)

//      val fireTime = value.ts + interval
//      fireTimeValueState.update(fireTime)
//      ctx.timerService().registerEventTimeTimer(fireTime)

      println(s"fire time: ${fireTime}")
    } else if (value.tmp < lastTemp) {
      println("clear")
      ctx.timerService().deleteEventTimeTimer(freeTimestamp)
      fireTimeValueState.clear()
    }
  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, Sensor, String]#OnTimerContext, out: Collector[String]): Unit = {
    out.collect("传感器" + ctx.getCurrentKey + "的温度连续" + interval/10 + "秒连续上升")
    fireTimeValueState.clear()
  }
}
