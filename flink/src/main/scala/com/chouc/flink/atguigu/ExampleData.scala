package com.chouc.flink.atguigu

import java.io.{BufferedOutputStream, DataOutputStream}
import java.net.{ServerSocket, Socket}
/**
 * @Title: ExampleData
 * @Package com.chouc.flink.atguigu
 * @Description:
 * @author chouc
 * @date 2021/2/5
 * @version V1.0
 */
class ExampleData {

}

case class Sensor(id: String, tmp: Double, ts: Long)


object ExampleData {
  def getSensorData(): List[Sensor] = {
    List(
      Sensor("id1", 9.1, 1612504440L),
      Sensor("id1", 9.2, 1612504441L),
      Sensor("id1", 9.3, 1612504442L),
      Sensor("id1", 9.4, 1612504443L),
      Sensor("id1", 9.5, 1612504444L),
      Sensor("id1", 9.6, 1612504445L),
      Sensor("id1", 9.1, 1612504446L),
      Sensor("id1", 9.2, 1612504447L),
      Sensor("id1", 9.3, 1612504448L),
      Sensor("id1", 9.4, 1612504449L),
      Sensor("id2", 9.1, 1612504440L),
      Sensor("id2", 9.2, 1612504441L),
      Sensor("id2", 9.3, 1612504442L),
      Sensor("id2", 9.4, 1612504443L),
      Sensor("id2", 9.5, 1612504444L),
      Sensor("id2", 9.6, 1612504445L),
      Sensor("id2", 9.1, 1612504446L),
      Sensor("id2", 9.2, 1612504447L),
      Sensor("id2", 9.3, 1612504448L),
      Sensor("id2", 9.4, 1612504449L)
    )
  }

  def main(args: Array[String]): Unit = {
    val server = new ServerSocket(8888)
    val socket =server.accept()
    val output = new BufferedOutputStream(socket.getOutputStream)
    val ids = Seq("abb2305a-6f7a-4925-8ae5-d5b7f64a2e65", "8723e15a-2549-41e5-a061-c37d739e96d9", "8723e15a-2549-41e5-a061-c37d739e96d9")
    for (i <- 1 to 10000) {
      val outString = s"${ids(i % 2)},${System.currentTimeMillis() + 1000 * i % 10},36.0"
      output.write(outString.toByte)
      Thread.sleep(1000)
    }
  }
}