package chouc.storm.count;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class MyCountBolt extends BaseRichBolt{
    OutputCollector collector;
    Map<String,Integer> map = new HashMap<String,Integer>();

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getString(0);
        int num = input.getInteger(1);
        if (map.containsKey(word)){
            map.put(word,map.get(word)+num);
        }else {
            map.put(word,num);
        }
        System.out.println("hash:"+map.hashCode()+"-count:"+map);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
