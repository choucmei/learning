package chouc.scala.cases

import scala.util.Random

case class SubmitTask(id: String, name: String)

case class HeartBeat(time: Long)

case object CheckTimeOutTask

object CaseDemo04 extends App {

  val arr = Array(CheckTimeOutTask, HeartBeat(12333), SubmitTask("0001", "task-0001"))


  val a = CheckTimeOutTask
  val b = CheckTimeOutTask

  arr(Random.nextInt(arr.length)) match {
    case SubmitTask(id, name) if (id.length < 2) => {
      println(s"$id, $name")
    }
    case HeartBeat(time) if (time < 10) => {
      println(time)
    }
    case CheckTimeOutTask => {
      println("check")
    }
    case _ => {
      println("not found")
    }
  }
}
