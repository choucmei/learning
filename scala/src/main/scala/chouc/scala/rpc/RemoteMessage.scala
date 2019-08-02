package chouc.scala.rpc

trait RemoteMessage extends Serializable

case class RegisterMessage(id:String, memory: Int, cores: Int)extends RemoteMessage

case object RegisterSuccess extends RemoteMessage

case object HeartBeatWorkToWorkMessage extends RemoteMessage

case object HeartBeatMasterToMasterMessage extends RemoteMessage

case class HeartBeatWorkToMasterMessage(id:String) extends RemoteMessage