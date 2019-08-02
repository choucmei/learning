package chouc.storm.kafka;

import com.google.gson.Gson;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class BoltRedis extends BaseRichBolt {
    private OutputCollector collector;
    private Gson gsonUtil;
    private OrderBean orderBeanTemp;
    private Jedis jedis;

    private final String SUM_3C_MONEY ="sum:3c:money";

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        System.out.println(" prepare-start");
        jedis = new Jedis("s1",6379);
        jedis.auth("123");
        this.collector = collector;
        gsonUtil = new Gson();

        /**
         * 防止最开始为空
         */
        String sum3CMoney = jedis.get(SUM_3C_MONEY);
        if (sum3CMoney==null||sum3CMoney.trim().equals("")){
            jedis.set(SUM_3C_MONEY,"0");
        }
    }

    @Override
    public void execute(Tuple input) {
        String line =  new String((byte[]) input.getValue(0));
        orderBeanTemp = gsonUtil.fromJson(line, OrderBean.class);
        String sum3CMoney = jedis.get(SUM_3C_MONEY);
        jedis.set(SUM_3C_MONEY,Integer.parseInt(sum3CMoney)+orderBeanTemp.getPayPrice()+"");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        System.out.println("m-declareOutputFields");
//        declarer.declare(new Fields("word","num"));
    }



    @Test
    public void testRedis(){
        Jedis jedis = new Jedis("s1",6379);
        jedis.auth("123");
        System.out.println(jedis.ping());;
    }



}
