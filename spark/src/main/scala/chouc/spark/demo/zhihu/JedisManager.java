package chouc.spark.demo.zhihu;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager {

    // jedis池
    public static JedisPool pool;

    // 静态代码初始化池配置
    static {
        //change "maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis" in all examples
        JedisPoolConfig config = new JedisPoolConfig();

        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(50);
        config.setMinIdle(0);
        config.setMaxTotal(50);

        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setMaxTotal(1000 * 100);

        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(5);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        try {
            //如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
            //请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
            pool = new JedisPool(config, "c3", 6379, 100000,"123456");
        } catch (Exception e) {
            throw new RuntimeException("redis 连接池初始化失败！");
        }
    }


    public static void process(String key,Integer num,String word){
        Jedis jedis = pool.getResource();
        jedis.zincrby(key,num,word);
        jedis.close();
    }

}
