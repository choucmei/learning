package chouc.storm.logAnalyze.app.callback;


import chouc.storm.logAnalyze.app.domain.BaseRecord;
import chouc.storm.logAnalyze.storm.dao.LogAnalyzeDao;
import chouc.storm.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;


public class HalfAppendCallBack implements Runnable {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) % 30 == 0) {
            String endTime = DateUtils.getDataTime(calendar);
            String startTime = DateUtils.before30Minute(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHalfAppendRecord(baseRecordList);
        }
    }
}
