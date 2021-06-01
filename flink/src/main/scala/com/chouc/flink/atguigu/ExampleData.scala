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

case class Sensor(id: String, ts: Long, tmp: Double)


object ExampleData {
  def getSensorData(): List[Sensor] = {
    List(
      Sensor("id1", 1612504440L, 9.1),
      Sensor("id1", 1612504441L, 9.2),
      Sensor("id1", 1612504442L, 9.3),
      Sensor("id1", 1612504443L, 9.4),
      Sensor("id1", 1612504444L, 9.5),
      Sensor("id1", 1612504445L, 9.6),
      Sensor("id1", 1612504446L, 9.1),
      Sensor("id1", 1612504447L, 9.2),
      Sensor("id1", 1612504448L, 9.3),
      Sensor("id1", 1612504449L, 9.4),
      Sensor("id2", 1612504440L, 9.1),
      Sensor("id2", 1612504441L, 9.2),
      Sensor("id2", 1612504442L, 9.3),
      Sensor("id2", 1612504443L, 9.4),
      Sensor("id2", 1612504444L, 9.5),
      Sensor("id2", 1612504445L, 9.6),
      Sensor("id2", 1612504446L, 9.1),
      Sensor("id2", 1612504447L, 9.2),
      Sensor("id2", 1612504448L, 9.3),
      Sensor("id2", 1612504449L, 9.4)
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