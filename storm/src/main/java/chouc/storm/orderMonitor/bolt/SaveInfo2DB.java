package chouc.storm.orderMonitor.bolt;

import chouc.storm.orderMonitor.domain.PaymentInfo;
import chouc.storm.orderMonitor.utils.OrderMonitorHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.List;

public class SaveInfo2DB extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String firstField = input.getFields().get(0);
        if ("orderId".equals(firstField)) {
            OrderMonitorHandler.saveTrigger(input.getStringByField("orderId"), (List<String>)input.getValueByField("triggerList"));
        }
        if ("paymentInfo".equals(firstField)) {
            OrderMonitorHandler.savePaymentInfo((PaymentInfo) input.getValueByField("paymentInfo"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
