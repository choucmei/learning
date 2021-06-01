package chouc.spark.hbase

import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * @Title: Spark2Hbase
 * @Package chouc.spark.hbase
 * @Description:
 * @author chouc
 * @date 2021/1/18
 * @version V1.0
 */
class Spark2Hbase {

}

object Spark2Hbase {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val sparkConf = new SparkConf();
    sparkConf.setMaster("local")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.registerKryoClasses(Array[Class[_]](classOf[ImmutableBytesWritable]))
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate();
    println(" Spark2Hbase path:" + args(0) + " output path:" + args(1))
    val df = spark.read.option("header", "true").csv(args(0))
    val cf = "f"
    val cols = df.columns
    val tmpData = df.rdd.map(
      row => {
        val rowkeyString = row.getAs[String]("name")
        val rowkeyByte = new ImmutableBytesWritable(Bytes.toBytes(rowkeyString))
        var keyValues = new collection.mutable.ListBuffer[KeyValue];

        for (column <- cols) {
          val kv: KeyValue = new KeyValue(
            Bytes.toBytes(rowkeyString),
            Bytes.toBytes(cf),
            Bytes.toBytes(column),
            Bytes.toBytes(row.getAs[String](column)))
          keyValues += (kv)
        }
        (rowkeyByte, keyValues)
      }).flatMapValues(_.iterator)
    val hbaseConf = HBaseConfiguration.create();
    hbaseConf.set("hbase.mapreduce.hfileoutputformat.table.name", "mxb:test")
    tmpData.sortBy(x => x._1, true)
      .saveAsNewAPIHadoopFile(args(1), classOf[ImmutableBytesWritable]
        , classOf[KeyValue], classOf[HFileOutputFormat2], conf = hbaseConf)

  }
}
