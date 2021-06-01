package com.chouc.flink.orc

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import org.apache.flink.orc.vector.Vectorizer
import org.apache.hadoop.hive.ql.exec.vector.{BytesColumnVector, DoubleColumnVector, LongColumnVector, VectorizedRowBatch}


/**
 * @Title: SensorV
 * @Package com.chouc.flink.orc
 * @Description:
 * @author chouc
 * @date 2021/2/7
 * @version V1.0
 */
class SensorVectorizer(schema: String) extends Vectorizer[Sensor](schema) {
  override def vectorize(element: Sensor, batch: VectorizedRowBatch): Unit = {
    val stringVector = batch.cols(0).asInstanceOf[BytesColumnVector]
    val longColVector = batch.cols(1).asInstanceOf[LongColumnVector]
    val doubleColVector = batch.cols(2).asInstanceOf[DoubleColumnVector]

    val row = batch.size

    stringVector.setVal(row, element.id.getBytes(StandardCharsets.UTF_8))
    longColVector.vector(row) = element.ts
    doubleColVector.vector(row) = element.temp

    this.addUserMetadata("userKey", ByteBuffer.wrap("hello".getBytes))
  }
}


