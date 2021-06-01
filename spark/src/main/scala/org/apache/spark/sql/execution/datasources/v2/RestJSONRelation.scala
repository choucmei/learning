package org.apache.spark.sql.execution.datasources.v2

import java.net.URL

import com.fasterxml.jackson.core.JsonFactory
import net.sf.json.JSONObject
import org.apache.http.client.fluent.Request
import org.apache.http.util.EntityUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.json.{CreateJacksonParser, JSONOptionsInRead, JsonInferSchema}
import org.apache.spark.sql.execution.SQLExecution
import org.apache.spark.sql.execution.datasources.json.JsonUtils
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Dataset, Row, SQLContext, SparkSession}


/**
 * @Title: RestJSONRelation
 * @Package org.apache.spark.sql.execution.datasources.v2
 * @Description:
 * @author chouc
 * @date 2021/2/3
 * @version V1.0
 */
class RestJSONRelation(
                        val inputRDD: Option[RDD[String]],
                        val parameters: Map[String, String],
                        val url: String,
                        val xPath: String,
                        val samplingRatio: Double,
                        val maybeDataSchema: Option[StructType]
                      )(@transient val sparkSession: SparkSession)
  extends BaseRelation
    with TableScan {
  override def sqlContext: SQLContext = {
    sparkSession.sqlContext
  }

  override def buildScan(): RDD[Row] = {
    inputRDD.getOrElse(createBaseRdd(Array(url))).map(v => Row(v))
  }

  override def schema: StructType = dataSchema

  private def createBaseRdd(inputPaths: Array[String]): RDD[String] = {
    val url = inputPaths.head
    val res = Request.Get(new URL(url).toURI).execute()
    val response = res.returnResponse()
    val content = EntityUtils.toString(response.getEntity)
//    if (response != null && response.getStatusLine.getStatusCode == 200) {
//      //这里是做数据抽取的，把data的数组给抽取出来
//      import scala.jdk.CollectionConverters
//      JSONObject.fromObject(content).getJSONArray(xPath).ge
//      sqlContext.sparkContext.makeRDD(.toArray.toSeq[String]);
//    } else {
//    }

    sqlContext.sparkContext.makeRDD(Seq())

  }

  lazy val dataSchema = {
    val parsedOptions = new JSONOptionsInRead(
      parameters,
      sparkSession.sessionState.conf.sessionLocalTimeZone,
      sparkSession.sessionState.conf.columnNameOfCorruptRecord)
    import sparkSession.implicits._
    val sampled: Dataset[String] = JsonUtils.sample(inputRDD.getOrElse(createBaseRdd(Array(url))).toDS(), parsedOptions)
    val rdd: RDD[InternalRow] = sampled.queryExecution.toRdd
    val rowParser = parsedOptions.encoding.map { enc =>
      CreateJacksonParser.internalRow(enc, _: JsonFactory, _: InternalRow)
    }.getOrElse(CreateJacksonParser.internalRow(_: JsonFactory, _: InternalRow))

    SQLExecution.withSQLConfPropagated(sparkSession) {
      new JsonInferSchema(parsedOptions).infer(rdd, rowParser)
    }
  }
}