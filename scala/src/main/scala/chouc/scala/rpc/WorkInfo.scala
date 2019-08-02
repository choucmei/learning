package chouc.scala.rpc

class WorkInfo(val id: String, val memory: Int, val cores: Int) {
  var lastHeartBeat: Long = _
}
