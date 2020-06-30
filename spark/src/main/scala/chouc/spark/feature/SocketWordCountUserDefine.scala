package chouc.spark.feature

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.listen.{DStreamListener}
import org.apache.spark.metric.{FailureNum, MetricMaster, MetricWork, SuccessNum}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.ui.dstream.{DStreamInfoPage, StreamingAdditionTab}
import org.apache.spark.ui.utils.WebUiRegister

object SocketWordCountUserDefine {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("SocketWordCountDstream")
      .setMaster("local[3]")
    val sparkContext = new SparkContext(sparkConf)
//    sparkConf.set("spark.ui.enabled","true")
    val sparkStreamContext = new StreamingContext(sparkContext,Seconds(1))
    WebUiRegister.attachTab(sparkContext,new StreamingAdditionTab(sparkStreamContext))
    val metric = MetricMaster.getMetricMaster(sparkConf)
    sparkContext.setLogLevel("WARN")
    val dstream01 = sparkStreamContext.socketTextStream("localhost",1010)
    dstream01.flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey((pre,cur)=>{
        pre + cur
      }).foreachRDD(rdd => {
        rdd.foreachPartition(iterator=>{
          iterator.foreach(v=>{
            if (v._1.startsWith("f")){
              MetricWork.report(FailureNum("mxb",v._2))
            } else {
              MetricWork.report(SuccessNum("cjl",v._2))
            }
          })
        })
      })
//    sparkStreamContext.addStreamingListener(new DStreamListener())
    val startThread = new Thread(){
      override def run(): Unit = {
        sparkStreamContext.start()
        sparkStreamContext.awaitTermination()
      }
    }
    startThread.start()

    StreamingContext.getActive() match {
      case Some(ssc) => ssc.stop(true)
      case None => println(" none")
    }

  }
}
