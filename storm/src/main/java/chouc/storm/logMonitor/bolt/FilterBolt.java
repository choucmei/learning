package chouc.storm.logMonitor.bolt;

import chouc.storm.logMonitor.domain.Message;
import chouc.storm.logMonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

//BaseRichBolt 需要手动调ack方法，BaseBasicBolt由storm框架自动调ack方法
public class FilterBolt extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(FilterBolt.class);
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取KafkaSpout发送出来的数据
        String line = input.getString(0);
        //获取kafka发送的数据，是一个byte数组
//        byte[] value = (byte[]) input.getValue(0);
        //将数组转化成字符串
//        String line = new String(value);
        //对数据进行解析
        // appid   content
        //1  error: Caused by: java.lang.NoClassDefFoundError: com/starit/gejie/dao/SysNameDao
        Message message = MonitorHandler.parser(line);
        if (message == null) {
            return;
        }
        if (MonitorHandler.trigger(message)) {
            collector.emit(new Values(message.getAppId(), message));
        }
        //定时更新规则信息
        MonitorHandler.scheduleLoad();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("appId", "message"));
    }
}
