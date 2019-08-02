package chouc.storm.orderMonitor.bolt;

import chouc.storm.orderMonitor.domain.PaymentInfo;
import chouc.storm.orderMonitor.utils.OrderMonitorHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.List;

public class PaymentInfoParserBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        PaymentInfo paymentInfo = (PaymentInfo) input.getValueByField("paymentInfo");
        if (paymentInfo == null) {
            return;
        }
        List<String> triggerList = OrderMonitorHandler.match(paymentInfo);
//        List<String> triggerList = new ArrayList<>();
        triggerList.add("12");
        triggerList.add("13");
        if (triggerList.size() > 0) {
            collector.emit(new Values(paymentInfo.getOrderId(), triggerList));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("orderId", "triggerList"));
    }

}
