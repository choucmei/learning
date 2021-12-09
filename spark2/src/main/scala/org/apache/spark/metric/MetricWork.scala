package org.apache.spark.metric

import java.net.InetAddress

import org.apache.spark.{SparkConf, SparkEnv}
import org.apache.spark.rpc.{RpcAddress, RpcEndpointRef, RpcEnv}

class MetricWork(masterRef:RpcEndpointRef) {



}
object MetricWork{

  val clientHost: String = InetAddress.getLocalHost().getHostAddress
  val rpcEnv = RpcEnv.create("Metric",clientHost,MetricMaster.ENDPOINT_PORT,new SparkConf(),SparkEnv.get.securityManager,false)
  val rpcAddress =new RpcAddress(MetricMaster.ENDPOINT_HOST,MetricMaster.ENDPOINT_PORT)
  val masterRef = rpcEnv.setupEndpointRef(rpcAddress, MetricMaster.ENDPOINT_NAME)

  def report(reportNum: ReportNum): Unit ={
    println(s"threadId:${Thread.currentThread().getId} report")
    masterRef.send(reportNum)
  }
}