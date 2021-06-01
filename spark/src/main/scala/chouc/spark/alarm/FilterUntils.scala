package chouc.spark.alarm

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @Title: FilterUntils
 * @Package chouc.spark.alarm
 * @Description:
 * @author chouc
 * @date 2021/2/24
 * @version V1.0
 */
object FilterUntils {


  val DATA_PATTERN = "yyyy-MM-dd"
  val TIME_PATTERN = "HH:mm:ss"
  val dateFormat: ThreadLocal[SimpleDateFormat] = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue(): SimpleDateFormat = {
      new SimpleDateFormat(DATA_PATTERN)
    }
  }
  val timeFormat: ThreadLocal[SimpleDateFormat] = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue(): SimpleDateFormat = {
      new SimpleDateFormat(TIME_PATTERN)
    }
  }


  // 过滤时间
  def filterDateTime(startDate: Date, endDate: Date, startTime: Date, endTime: Date, currentDTStr: String): Boolean = {
    val curDateTime = currentDTStr.split(" ")
    val curTime = timeFormat.get().parse(curDateTime(1))
    val curDate = dateFormat.get().parse(curDateTime(0))
    curDate.before(endDate) && curDate.after(startDate) && curTime.before(endTime) && curTime.after(startTime)
  }

  // 过滤电话
  def filterPhone(attentionPhones: Seq[String],
                  callers: Seq[String], calleds: Seq[String],
                  callerAttrs: Seq[String], calledAttrs: Seq[String],
                  currentCaller: String, currentCalled: String,
                  currentCallerAttr: String, currentCalledAttr: String): Boolean = {
    val isCaller = attentionPhones.contains(currentCaller)
    val isCalled = attentionPhones.contains(currentCalled)
    if (callers.isEmpty && calleds.isEmpty) {
      (isCaller && calledAttrs.contains(currentCalledAttr)) || (currentCallerAttr.contains(currentCaller) && isCalled)
    } else if (callerAttrs.isEmpty && calledAttrs.isEmpty) {
      (attentionPhones.contains(currentCaller) && calleds.contains(currentCalled)) || (callers.contains(currentCaller) && attentionPhones.contains(currentCalled))
    } else {
      isCaller || isCalled
    }
  }


  def filterPerson(attentionSfzhs: Seq[String], sfzh: String): Boolean = {
    attentionSfzhs.contains(sfzh)
  }

  // 过滤区域
  def filterRegion(srcRegions: Seq[String], tgtRegions: Seq[String], curSrcRegion: String, curTgtRegion: String): Boolean = {
    srcRegions.contains(curSrcRegion) && tgtRegions.contains(curTgtRegion)
  }

  // 过滤地区
  def filterRegionName(srcRegionNames: Seq[String], tgtRegionNames: Seq[String], curSrcRegionName: String, curTgtRegionName: String): Boolean = {
    srcRegionNames.contains(curSrcRegionName) && tgtRegionNames.contains(curTgtRegionName)
  }


}