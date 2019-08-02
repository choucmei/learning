package chouc.storm.logAnalyze.storm.spout;


import chouc.storm.logAnalyze.storm.domain.LogMessage;
import com.google.gson.Gson;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 *随机产生消息发送出去
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private TopologyContext context;
    private List<LogMessage> list ;

    public RandomSpout() {
        super();
    }

    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.context = context;
        this.collector = collector;
        list = new ArrayList();
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
    }

    @Override
    /**
     * 发送消息
     */
    public void nextTuple() {
        final Random rand = new Random();
        LogMessage msg = list.get(rand.nextInt(4));
        this.collector.emit(new Values(new Gson().toJson(msg)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("paymentInfo"));
    }
}
