package chouc.scala.rpc

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class Worker(val masterHost: String, val masterPort: Int) extends Actor{
  val id:String= UUID.randomUUID().toString;
  val HEART_INTERVAL = 10000

  var master : ActorSelection  = _
  override def preStart(): Unit = {
    //在master启动时会打印下面的那个协议, 可以先用这个做一个标志, 连接哪个master
    //继承actor后会有一个context, 可以通过它来连接
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master") //需要有/user, Master要和master那边创建的名字保持一致
    master ! RegisterMessage(id,1024,4)
  }

  override def receive = {
    case RegisterSuccess => {
      import context.dispatcher
      context.system.scheduler.schedule(0 millis,2000 millis,self,HeartBeatWorkToWorkMessage)
    }
    case HeartBeatWorkToWorkMessage => {
      master ! HeartBeatWorkToMasterMessage(id)
      println(s" work heartbeat $id")
    }
  }
}

object Worker{
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("WorkerSystem", config)
    val worker = actorSystem.actorOf(Props(new Worker(args(2),args(3).toInt)), "Worker")
    actorSystem.awaitTermination()
  }
}
