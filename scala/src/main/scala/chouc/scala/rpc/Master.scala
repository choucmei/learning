package chouc.scala.rpc
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._


class Master extends Actor{

  val idToWorkInfo = new mutable.HashMap[String,WorkInfo]()
  val works = new mutable.HashSet[WorkInfo]()
  val checkHeartbeatTime = 2000

  override def preStart(): Unit = {
    import context.dispatcher
    context.system.scheduler.schedule(0 millis,checkHeartbeatTime millis,self,HeartBeatMasterToMasterMessage)
  }

  override def receive: Receive = {
    case RegisterMessage(id,memory,cores) => {
      if (!idToWorkInfo.contains(id)){
        var currentWork = new WorkInfo(id,memory,cores);
        idToWorkInfo(id) = currentWork
        works += currentWork
        sender ! RegisterSuccess
      }
    }
    case HeartBeatWorkToMasterMessage(id) => {
      if(idToWorkInfo.contains(id)){
        var currentWork = idToWorkInfo(id)
        currentWork.lastHeartBeat = System.currentTimeMillis()
      }
//      idToWorkInfo(id) = currentWork
    }
    case HeartBeatMasterToMasterMessage => {
      val toRemove = works.filter(x=>System.currentTimeMillis() - x.lastHeartBeat > checkHeartbeatTime)
      for (work <- toRemove){
        works -= work
        idToWorkInfo.remove(work.id)
      }
      println(works.size)
    }
  }

}

object Master{
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
    val actorSystem = ActorSystem("MasterSystem", config)
    val master = actorSystem.actorOf(Props[Master], "Master")//Master主构造器会执行
    master ! "hello"  //发送信息
    actorSystem.awaitTermination()  //让进程等待着, 先别结束
  }
}



