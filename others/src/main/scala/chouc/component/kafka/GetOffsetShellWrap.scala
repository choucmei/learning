package chouc.component.kafka


import kafka.api.{PartitionOffsetRequestInfo, _}
import kafka.client.ClientUtils
import kafka.common.{OffsetMetadataAndError, TopicAndPartition}
import kafka.consumer.SimpleConsumer
import kafka.network.BlockingChannel
import kafka.tools.ConsumerOffsetChecker.debug
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.exception.ZkNoNodeException
import org.apache.kafka.common.protocol.Errors
import org.apache.kafka.common.security.JaasUtils
import org.slf4j.LoggerFactory

import scala.collection.{immutable, mutable}

/**
  * @Title: GetOffsetShellWrap
  * @Package chouc.component.kafka
  * @Description:
  * @author chouc
  * @date 10/17/19
  * @version V1.0
  */
class GetOffsetShellWrap {
  private val logger = LoggerFactory.getLogger(classOf[GetOffsetShellWrap])

  private val hosts = Map("192.168.90.97" -> 9092)
  private val timeOut = 30000
  private val bufferSize = 64 * 1000000
  private var clientID = s"kafka_info_client_${System.currentTimeMillis}"


  def getLatestOffset(topic: String): mutable.HashMap[Integer, Long] = { //kafka.api.OffsetRequest.LatestTime() = -1
    getTopicOffset(topic, kafka.api.OffsetRequest.LatestTime)
  }

  def getEarliestOffset(topic: String): mutable.HashMap[Integer, Long] = { //kafka.api.OffsetRequest.EarliestTime() = -2
    getTopicOffset(topic, kafka.api.OffsetRequest.EarliestTime)
  }

  /** *
    * 获取指定 topic 的所有分区 offset
    *
    * @param topic
    * @param whichTime 要获取offset的时间,-1 最新，-2 最早
    * @return
    */
  def getTopicOffset(topic: String, whichTime: Long): mutable.HashMap[Integer, Long] = {
    val offsets = new mutable.HashMap[Integer, Long]
    val leaders = this.findLeader(hosts, topic)
    for ((part: Int, matedata: PartitionMetadata) <- leaders) {
      val leadBroker = matedata.leader.get.host
      val leadPort = matedata.leader.get.port
      val consumer = new SimpleConsumer(leadBroker, leadPort, timeOut, bufferSize, clientID)
      val partationOffset = this.getPartationOffset(consumer, topic, part, whichTime)
      offsets.put(part, partationOffset)
    }
    offsets
  }


  def getGroupOffset(zkUrl: String, topic: String, groupId: String): mutable.HashMap[Int, Long] = {
    val offsets = new mutable.HashMap[Integer, Long]
    val leaders = this.findLeader(hosts, topic)
    val topicAndPartitions = leaders.map(_._1).map(TopicAndPartition(topic, _)).toSeq

    val zkUtils = ZkUtils(zkUrl, 30000, 30000, JaasUtils.isZkSecurityEnabled)
    val channel: BlockingChannel = ClientUtils.channelToOffsetManager(groupId, zkUtils, 30000, 30000)

    channel.send(OffsetFetchRequest(groupId, topicAndPartitions))
    val offsetFetchResponse = OffsetFetchResponse.readFrom(channel.receive().payload())
    val offsetMap: mutable.Map[TopicAndPartition, Long] = mutable.Map()

    channel.send(OffsetFetchRequest(groupId, topicAndPartitions))
    offsetFetchResponse.requestInfo.foreach { case (topicAndPartition, offsetAndMetadata) =>
      if (offsetAndMetadata == OffsetMetadataAndError.NoOffset) {
        val topicDirs = new ZKGroupTopicDirs(groupId, topicAndPartition.topic)
        try {
          val offset = zkUtils.readData(topicDirs.consumerOffsetDir + "/%d".format(topicAndPartition.partition))._1.toLong
          offsetMap.put(topicAndPartition, offset)
        } catch {
          case z: ZkNoNodeException =>
            if (zkUtils.pathExists(topicDirs.consumerOffsetDir))
              offsetMap.put(topicAndPartition, -1)
            else
              throw z
        }
      }
      else if (offsetAndMetadata.error == Errors.NONE.code)
        offsetMap.put(topicAndPartition, offsetAndMetadata.offset)
      else {
        println("Could not fetch offset for %s due to %s.".format(topicAndPartition, Errors.forCode(offsetAndMetadata.error).exception))
      }
    }
    val result = new mutable.HashMap[Int,Long]()
    offsetMap.foreach(partitionAndOffset=>{
      result(partitionAndOffset._1.partition)=partitionAndOffset._2
    })
    channel.disconnect()
    result
  }

  /** *
    * 获取 offset
    *
    * @param consumer  SimpleConsumer
    * @param topic     topic
    * @param partition partition
    * @param whichTime 要获取offset的时间,-1 最新，-2 最早
    * @return
    */
  private def getPartationOffset(consumer: SimpleConsumer, topic: String, partition: Int, whichTime: Long): Long = {
    val topicAndPartition = new TopicAndPartition(topic, partition)
    val requestInfo = new mutable.HashMap[TopicAndPartition, PartitionOffsetRequestInfo]
    requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1))
    //PartitionOffsetRequestInfo(long time, int maxNumOffsets) 中的第二个参数maxNumOffsets，没弄明白是什么意思，但是测试后发现传入1 时返回whichTime 对应的offset，传入2 返回一个包含最大和最小offset的元组
    val request = OffsetRequest(requestInfo.toMap, OffsetRequest.CurrentVersion, 0, consumer.clientId)
    val response = consumer.getOffsetsBefore(request)
    if (response.hasError) {
      logger.error("Error fetching data Offset Data the Broker. Reason:{}", response.hasError)
      return 0
    }
    val offsets = response.partitionErrorAndOffsets(TopicAndPartition(topic, partition))
    offsets.offsets(0)
  }

  /** *
    * 获取每个 partition 元数据信息
    *
    * @param bootstraps (host,port)
    * @param topic      topic
    * @return
    */
  private def findLeader(bootstraps: Map[String, Int], topic: String) = {
    val map = new mutable.HashMap[Int, PartitionMetadata]()
    for ((key, value) <- bootstraps) {
      var consumer: SimpleConsumer = null
      try {
        consumer = new SimpleConsumer(key, value, timeOut, bufferSize, clientID)
        val req = new TopicMetadataRequest(versionId = 0, 0, clientID, Seq(topic))
        val resp = consumer.send(req)
        val metaData = resp.topicsMetadata
        for (item <- metaData) {
          for (part <- item.partitionsMetadata) {
            map.put(part.partitionId, part)
          }
        }
      } catch {
        case e: Exception =>
          logger.error("Error communicating with Broker [{}] to find Leader for [{}] Reason: ", key, topic, e)
      } finally if (consumer != null) consumer.close()
    }
    map
  }
}

object GetOffsetShellWrap {
  def main(args: Array[String]): Unit = {
    val getOffsetShellWrap: GetOffsetShellWrap = new GetOffsetShellWrap
    val a = getOffsetShellWrap.getLatestOffset("impression")
    println(a)
    val b = getOffsetShellWrap.getGroupOffset("smaster01:2181","impression","testmxb")
    println(b)
  }
}
