package org.apache.spark.sql.execution.datasources.v2

import java.util

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.connector.catalog.{Table, TableProvider}
import org.apache.spark.sql.connector.expressions.Transform
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, RelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.util.CaseInsensitiveStringMap

/**
 * @Title: RestDataSource
 * @Package org.apache.spark.sql.execution.datasources.v2
 * @Description:
 * @author chouc
 * @date 2021/2/3
 * @version V1.0
 */
class RestDataSource extends RelationProvider with DataSourceRegister {

  override def shortName(): String = "rest"

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    //因为我们并需要用户提供schema
    //而是从JSON格式数据自己自己推导出来的
    // 所以这里有个采样率的概念
    val samplingRatio = parameters.get("samplingRatio").map(_.toDouble).getOrElse(1.0)
    // 还记得DataSource的 path么？ 理论上是应该通过那个传递过来的，然而
    //这里是直接通过potions传递过来的。
    val url = parameters.getOrElse("url", "")
    // 我们需要能够对通过XPATH语法抽取我们要的数据，比如
    //前面的例子，我们需要能够抽取出data那个数组
    val xPath = parameters.getOrElse("xPath", "$")
    //这里是核心
    new RestJSONRelation(None,parameters,url,xPath,samplingRatio,None)(sqlContext.sparkSession)
  }
}
