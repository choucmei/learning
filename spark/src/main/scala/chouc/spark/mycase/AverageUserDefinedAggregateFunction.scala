package chouc.spark.mycase

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * @Title: UDAFunctions
  * @Package chouc.spark.mycase
  * @Description:
  * @author chouc
  * @date 10/22/19
  * @version V1.0
  */
class AverageUserDefinedAggregateFunction extends UserDefinedAggregateFunction{
  override def inputSchema: StructType = {
    StructType(StructField("input", LongType) :: Nil)
  }

  override def bufferSchema: StructType = {
    StructType(StructField("input", LongType) :: Nil)
  }

  override def dataType: DataType = {
    LongType
  }

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if (input.isNullAt(0)) return
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}
