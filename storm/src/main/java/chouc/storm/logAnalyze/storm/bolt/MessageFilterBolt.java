package chouc.storm.logAnalyze.storm.bolt;

import chouc.storm.logAnalyze.storm.domain.LogMessage;
import chouc.storm.logAnalyze.storm.utils.LogAnalyzeHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class MessageFilterBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取KafkaSpout发送出来的数据
        String line = input.getString(0);
        //对数据进行解析
        LogMessage logMessage = LogAnalyzeHandler.parser(line);
        if (logMessage == null || !LogAnalyzeHandler.isValidType(logMessage.getType())) {
            return;
        }
        collector.emit(new Values(logMessage.getType(), logMessage));
        //定时更新规则信息
        LogAnalyzeHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //根据点击内容类型将日志进行区分
        declarer.declare(new Fields("type", "message"));
    }


}
