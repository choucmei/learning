package org.apache.spark.listen

import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted, StreamingListenerBatchStarted, StreamingListenerBatchSubmitted, StreamingListenerOutputOperationCompleted, StreamingListenerOutputOperationStarted, StreamingListenerReceiverError, StreamingListenerReceiverStarted, StreamingListenerReceiverStopped, StreamingListenerStreamingStarted}

class DStreamListener extends StreamingListener{
  /** Called when the streaming has been started */
  override def onStreamingStarted(streamingStarted: StreamingListenerStreamingStarted): Unit = {
    println(" streaming started")
  }


  /** Called when a receiver has been started */
  override def onReceiverStarted(receiverStarted: StreamingListenerReceiverStarted): Unit = {
    receiverStarted.receiverInfo.streamId
  }

  /** Called when a receiver has reported an error */
  override def onReceiverError(receiverError: StreamingListenerReceiverError) { }

  /** Called when a receiver has been stopped */
  override def onReceiverStopped(receiverStopped: StreamingListenerReceiverStopped) { }

  /** Called when a batch of jobs has been submitted for processing. */
  override def onBatchSubmitted(batchSubmitted: StreamingListenerBatchSubmitted): Unit = {

  }

  /** Called when processing of a batch of jobs has started.  */
  override def onBatchStarted(batchStarted: StreamingListenerBatchStarted): Unit = {
    println(s"batchStarted soutputOperationInfos ${batchStarted.batchInfo.outputOperationInfos}")
    println(s"batchStarted streamIdToInputInfo ${batchStarted.batchInfo.streamIdToInputInfo}")
    println(s"batchStarted numRecords ${batchStarted.batchInfo.numRecords}")
  }

  /** Called when processing of a batch of jobs has completed. */
  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    println(s"soutputOperationInfos ${batchCompleted.batchInfo.outputOperationInfos}")
    println(s"streamIdToInputInfo ${batchCompleted.batchInfo.streamIdToInputInfo}")
    println(s"numRecords ${batchCompleted.batchInfo.numRecords}")
//    println(s"streamIdToInputInfo ${batchCompleted.batchInfo.}")
//    println(s"DStreamListener batchCompleted.batchInfo.numRecords ${batchCompleted.batchInfo.numRecords}")
//    println(s"DStreamListener batchCompleted.batchInfo.totalDelay ${batchCompleted.batchInfo.totalDelay}")
//    println(s"DStreamListener batchCompleted.batchInfo.batchTime ${batchCompleted.batchInfo.batchTime}")
//    println(s"DStreamListener batchCompleted.batchInfo.processingEndTime ${batchCompleted.batchInfo.processingEndTime}")
//    println(s"DStreamListener batchCompleted.batchInfo.processingStartTime ${batchCompleted.batchInfo.processingStartTime}")
  }

  /** Called when processing of a job of a batch has started. */
  override def onOutputOperationStarted(
                                outputOperationStarted: StreamingListenerOutputOperationStarted): Unit = {
//    println(s"outputOperationStarted.outputOperationInfo.id ${outputOperationStarted.outputOperationInfo.id}")
//    println(s"outputOperationStarted.outputOperationInfo.description ${outputOperationStarted.outputOperationInfo.description}")
//    println(s"outputOperationStarted.outputOperationInfo.name ${outputOperationStarted.outputOperationInfo.name}")
//    println(s"outputOperationStarted.outputOperationInfo.batchTime ${outputOperationStarted.outputOperationInfo.batchTime}")


  }

  /** Called when processing of a job of a batch has completed. */
  override def onOutputOperationCompleted(
                                  outputOperationCompleted: StreamingListenerOutputOperationCompleted): Unit = {

    println(s"outputOperationStarted.outputOperationInfo.id ${outputOperationCompleted.outputOperationInfo.id}")
    println(s"outputOperationStarted.outputOperationInfo.description ${outputOperationCompleted.outputOperationInfo.description}")
    println(s"outputOperationStarted.outputOperationInfo.name ${outputOperationCompleted.outputOperationInfo.name}")
    println(s"outputOperationStarted.outputOperationInfo.batchTime ${outputOperationCompleted.outputOperationInfo.batchTime}")



  }
}
