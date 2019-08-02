package chouc.storm.logMonitor.bolt;

import chouc.storm.logMonitor.domain.Message;
import chouc.storm.logMonitor.domain.Record;
import chouc.storm.logMonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.springframework.beans.BeanUtils;

//BaseRichBolt 需要手动调ack方法，BaseBasicBolt由storm框架自动调ack方法
public class PrepareRecordBolt extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(PrepareRecordBolt.class);

    public void execute(Tuple input, BasicOutputCollector collector) {
        Message message = (Message) input.getValueByField("message");
        String appId = input.getStringByField("appId");
        //将触发规则的信息进行通知
        MonitorHandler.notifly(appId, message);
        Record record = new Record();
        try {
            BeanUtils.copyProperties(record, message);
            collector.emit(new Values(record));
        } catch (Exception e) {

        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }

}
