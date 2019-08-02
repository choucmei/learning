package chouc.storm.logMonitor.bolt;

import chouc.storm.logMonitor.domain.Record;
import chouc.storm.logMonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class SaveMessage2MySql extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(SaveMessage2MySql.class);
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField("record");
        MonitorHandler.save(record);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}

