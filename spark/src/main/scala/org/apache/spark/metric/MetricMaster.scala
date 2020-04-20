package org.apache.spark.metric

import java.net.InetAddress

import org.apache.spark.rpc.{RpcEndpoint, RpcEndpointRef, RpcEnv}
import org.apache.spark.{SparkConf, SparkEnv}

class MetricMaster(_rpcEnv:RpcEnv) extends RpcEndpoint{
  override val rpcEnv: RpcEnv = _rpcEnv

  override def receive: PartialFunction[Any, Unit] = {
    case s:SuccessNum => {
      println(s"threadId:${Thread.currentThread().getId} SuccessNum topic:${s.topic} num:${s.num}")
    }
    case f:FailureNum => {
      println(s"threadId:${Thread.currentThread().getId} FailureNum topic:${f.topic} num:${f.num}")
    }
  }
}
object MetricMaster{
  val ENDPOINT_NAME = "CC_METRIC_MASTER";
  val ENDPOINT_HOST = InetAddress.getLocalHost().getHostAddress
  val ENDPOINT_PORT = 9880

  def getMetricMaster(sparkConf: SparkConf): RpcEndpointRef ={
    val rpcEnv = RpcEnv.create("Metric-Client",MetricMaster.ENDPOINT_HOST,MetricMaster.ENDPOINT_PORT,sparkConf,SparkEnv.get.securityManager,false)
    val rpcEndpoint = rpcEnv.setupEndpoint(MetricMaster.ENDPOINT_NAME, new MetricMaster(rpcEnv))
    rpcEndpoint
  }
}

trait ReportNum
case class SuccessNum(topic:String,num:Long) extends Serializable with ReportNum
case class FailureNum(topic:String,num:Long) extends Serializable with ReportNum