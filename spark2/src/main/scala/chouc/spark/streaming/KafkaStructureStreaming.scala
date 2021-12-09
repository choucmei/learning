package chouc.spark.streaming

import java.sql.Timestamp

import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.slf4j.LoggerFactory

/**
  * @Title: KafkaStructureStreaming
  * @Package chouc.spark.structure.streaming
  * @Description:
  * @author chouc
  * @date 7/31/19
  * @version V1.0
  */
class KafkaStructureStreaming {

}

object KafkaStructureStreaming {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val df = spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "smaster01:9092")
      .option("subscribe", "impression,terminalerror,terminalclick,adrequest,adflow,leftflow,rtbflow")
      .option("startingOffsets", "latest")
      .load()

    val sink = new RealTimeSink
    val q = df.writeStream.foreach(new ForeachWriter[Row]() {
      override def open(partitionId: Long, version: Long): Boolean = {
        sink.open(partitionId: Long, version: Long)
      }

      override def process(value: Row): Unit = {
        sink.process(value)
      }

      override def close(errorOrNull: Throwable): Unit = {
        sink.close(errorOrNull)
      }
    }).start()
    q.awaitTermination()
  }
}


class RealTimeSink extends Serializable {
  val log = LoggerFactory.getLogger(classOf[KafkaStructureStreaming])
  @transient
  val nRealTimSink = new NRealTimSink
  def open(partitionId: Long, version: Long): Boolean = {
    println("*************************open*********************************")
    true
  }

  def process(value: Row): Unit = {
    RealTimeSink.process(value)
    println("*************************process*********************************")
    val time: Timestamp = value.getAs[Timestamp]("timestamp")
//    nRealTimSink.process(value)
    log.info("{}",time)
  }

  def close(errorOrNull: Throwable): Unit = {
    println("*************************close*********************************")
  }

}

object RealTimeSink {
  val nRealTimSink = new NRealTimSink
  def process(value:Row): Unit ={
    nRealTimSink.process(value)
  }

  def main(args: Array[String]): Unit = {
    val sink = new RealTimeSink
    val write = new ForeachWriter[Row]() {
      override def open(partitionId: Long, version: Long): Boolean = {
        sink.open(partitionId: Long, version: Long)
      }

      override def process(value: Row): Unit = {
        sink.process(value)
      }

      override def close(errorOrNull: Throwable): Unit = {
        sink.close(errorOrNull)
      }
    }

    import java.io.FileOutputStream
    import java.io.ObjectOutputStream
    val fileOut: FileOutputStream = new FileOutputStream("/Users/chouc/Desktop/file/obj.info")
    val out: ObjectOutputStream = new ObjectOutputStream(fileOut)
    out.writeObject(sink)
    out.close()
    fileOut.close()

  }
}
class NRealTimSink {
  def process(value: Row): Unit = {
    println("*************************NRealTimSinkprocess*********************************")
//    val time: Long = value.getAs[Long]("timestamp")
//    println(time)
  }
}