package org.apache.spark.example

import scala.math.random

/**
  * @Title: LocalPi
  * @Package org.apache.spark.example
  * @Description:
  * @author chouc
  * @date 10/23/19
  * @version V1.0
  */
object LocalPi {
  def main(args: Array[String]) {
    var count = 0
    for (i <- 1 to 100000) {
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y <= 1) count += 1
    }
    println(s"Pi is roughly ${4 * count / 100000.0}")
  }
}
