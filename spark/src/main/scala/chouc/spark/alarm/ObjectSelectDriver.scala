package chouc.spark.alarm

import com.alibaba.fastjson.{JSON, JSONArray}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Title: Filter
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/2/24
 * @version V1.0
 */
class ObjectSelectDriver {

}

object ObjectSelectDriver {

  def loadPersonAndPhone(condition: String): DataFrame = {
    //mysql params
    val dbDatabase = "418pro"
    val dbJdbcUrl = "jdbc:mysql://localhost:3306"
    val dbUser = "root"
    val dbPassword = "HkilDiYoPRwjh"

    val sparkSession = SparkSession.builder().master("local").getOrCreate()
    val conditionJson = JSON.parseObject(condition)
    val controls = Option(conditionJson.getJSONArray("control_level")).getOrElse(new JSONArray()).toArray
    val sexs = Option(conditionJson.getJSONArray("archive_sex")).getOrElse(new JSONArray()).toJavaList(classOf[String]).toArray()
    val ageRange = Option(conditionJson.getJSONArray("archive_age_range")).getOrElse(new JSONArray()).toJavaList(classOf[Int]).toArray()
    val idcards = Option(conditionJson.getJSONArray("archive_idcard_number")).getOrElse(new JSONArray()).toJavaList(classOf[String]).toArray()
    val householdLocation = Option(conditionJson.getJSONArray("archive_household_Location")).getOrElse(new JSONArray()).toJavaList(classOf[String]).toArray()
    val actualLocation = Option(conditionJson.getJSONArray("archive_actual_location")).getOrElse(new JSONArray()).toJavaList(classOf[String]).toArray()

    // 条件拼接
    val sb = new StringBuilder(" 1=1 ")
    val op = " or "
    // 拼接 控制级别
    if (controls.size > 0) {
      sb.append(op)
      if (controls.size == 1) {
        sb.append("tag_name = ").append("'").append(controls(0)).append("'")
      } else {
        sb.append("tag_name in").append(controls.mkString(" ('", "','", "')"))
      }
    }
    // 拼接性别条件
    if (sexs.size > 0) {
      sb.append(op)
      if (sexs.size == 1) {
        sb.append("xb = ").append("'").append(sexs(0)).append("'")
      } else {
        sb.append("xb in").append(sexs.mkString("('", "','", "')"))
      }
    }
    //拼接年龄条件
    if (ageRange.size > 0) {
      sb.append(op)
      if (ageRange.size == 1) {
        sb.append("if(datediff(CURRENT_DATE,CONCAT(substr(CURRENT_DATE,0,4),substr(csrq,5,7)))>=0,(substr(CURRENT_DATE,0,4) - substr(csrq,0,4)),(substr(CURRENT_DATE,0,4) - substr(csrq,0,4)-1)) = ").append("'").append(sexs(0)).append("'")
      } else {
        sb.append("if(datediff(CURRENT_DATE,CONCAT(substr(CURRENT_DATE,0,4),substr(csrq,5,7)))>=0,(substr(CURRENT_DATE,0,4) - substr(csrq,0,4)),(substr(CURRENT_DATE,0,4) - substr(csrq,0,4)-1)) BETWEEN ").append(ageRange(0)).append(" and ").append(ageRange(1))
      }
    }
    //拼接身份证号码条件
    if (idcards.size > 0) {
      sb.append(op)
      if (sexs.size == 1) {
        sb.append("sfzh = ").append("'").append(idcards(0)).append("'")
      } else {
        sb.append("sfzh in").append(idcards.mkString("('", "','", "')"))
      }
    }
    println(sb.toString())

    val personDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select id as archive_id,sfzh,xm,xb,csrq from ${dbDatabase}.bbd_archives")
    val carDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select pzhm,archive_id from ${dbDatabase}.bbd_archives_jtgj where pzhm is not null")
    val phoneDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select account as phone,archive_id from ${dbDatabase}.bbd_archives_account where account_type='手机号'")
    val personLinkOrgZrrDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select organization_id,zrr_id,archive_id from ${dbDatabase}.bbd_archives_lkzrr_relationship")
    val orgDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select id as organization_id,org_name from ${dbDatabase}.bbd_organization")
    val zrrDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select id as zrr_id,full_name as zrr_name from ${dbDatabase}.bbd_user")
    val personLinkTagDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select tag_id,archive_id from ${dbDatabase}.bbd_archives_tag_relationship")
    val tagDf = getJdbcDataFrame(sparkSession, dbJdbcUrl, dbUser, dbPassword, s"select tag_name,id as tag_id,classification from ${dbDatabase}.bbd_archives_tags ")

    //  join archive_id 对应的tag信息
    val tagsArchiveDf = personLinkTagDf.join(tagDf, Seq("tag_id"), "left")
      .select("archive_id", "tag_name", "classification")
    //  join 人 对应的tag信息
    val personWithTagsDf = personDf.join(tagsArchiveDf, Seq("archive_id"), "left")
      .select("archive_id", "tag_name", "classification", "sfzh", "xm")
    // 过滤
    val selectedPersonDf = personWithTagsDf.where(sb.toString())
    // join 人-车牌
    val selectedPersonWithCarDf = selectedPersonDf.join(carDf, Seq("archive_id"), "left")
      .select("archive_id", "archive_id", "tag_name", "classification", "sfzh", "xm", "pzhm")

    // join 人-车牌-电话
    val selectedPersonWithCarPhoneDf = selectedPersonWithCarDf.join(phoneDf, Seq("archive_id"), "left")
      .select("archive_id", "archive_id", "tag_name", "classification", "sfzh", "xm", "pzhm", "phone")

    // join 人-车牌-电话-责任组织
    val selectedPersonWithCarOrgDf = selectedPersonWithCarPhoneDf.join(personLinkOrgZrrDf, Seq("archive_id"), "left")
      .select("archive_id", "archive_id", "tag_name", "classification", "sfzh", "xm", "pzhm", "phone", "organization_id", "zrr_id")
      .join(orgDf, Seq("organization_id"), "left")
      .select("archive_id", "archive_id", "tag_name", "classification", "sfzh", "xm", "pzhm", "phone", "organization_id", "org_name", "zrr_id")
    // join 人-车牌-电话-责任组织-责任人
    val selectedAll = selectedPersonWithCarOrgDf.join(zrrDf, Seq("zrr_id"), "left")
      .select("archive_id", "sfzh", "xm", "tag_name", "classification", "pzhm", "phone", "organization_id", "org_name", "zrr_id", "zrr_name")
    selectedAll.show()
    selectedAll
  }

  def getJdbcDataFrame(spark: SparkSession, dbJdbcUrl: String, dbUser: String, dbPassword: String, sql: String): DataFrame = {
    println(sql)
    val jdbcDataFrame = spark.read
      .format("jdbc")
      .option("url", dbJdbcUrl)
      .option("query", sql)
      .option("user", dbUser)
      .option("password", dbPassword)
      .load()
    jdbcDataFrame
  }

  def main(args: Array[String]): Unit = {
    import org.apache.spark.sql.functions._
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getName)
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sparkContext = new SparkContext(sparkConf)
    val condition = "{\"type_name\":\"模型自定义人员\",\"control_type\":\"SZ\",\"control_level\":[],\"archive_sex\":[\"男\"],\"archive_age_range\":[],\"archive_household_Location\":\"\",\"archive_actual_location\":\"\",\"archive_idcard_number\":[\"\"]}"
    val df = loadPersonAndPhone(condition)
    val person2Info = df.groupBy("sfzh").agg(concat_ws(",", collect_set("xm")).alias("xm"),
      concat_ws(",", collect_set("tag_name")).alias("tag_name"),
      concat_ws(",", collect_set("classification")).alias("classification"),
      concat_ws(",", collect_set("pzhm")).alias("pzhm"),
      concat_ws(",", collect_set("phone")).alias("phone"),
      concat_ws(",", collect_set("organization_id")).alias("organization_id"),
      concat_ws(",", collect_set("org_name")).alias("org_name"),
      concat_ws(",", collect_set("zrr_id")).alias("zrr_id"),
      concat_ws(",", collect_set("zrr_name")).alias("zrr_name")).show()
  }
}