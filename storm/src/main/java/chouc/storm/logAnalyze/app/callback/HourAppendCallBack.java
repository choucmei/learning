package chouc.storm.logAnalyze.app.callback;


import chouc.storm.logAnalyze.app.domain.BaseRecord;
import chouc.storm.logAnalyze.storm.dao.LogAnalyzeDao;
import chouc.storm.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

public class HourAppendCallBack implements Runnable {
    @Override
    public void run()  {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.MINUTE) == 59) {
            //获取所有的增量数据
            String endTime = DateUtils.getDataTime(calendar);
            String startTime = DateUtils.beforeOneHour(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHourAppendRecord(baseRecordList);
        }
    }
}