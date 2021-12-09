package org.apache.spark.ui.dstream

import org.apache.spark.internal.Logging
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.ui.SparkUITab

class StreamingAdditionTab(ssc:StreamingContext) extends SparkUITab(ssc.sparkContext.ui.get,"streamingAddition") with Logging {

  attachPage(new DStreamInfoPage(this))

}