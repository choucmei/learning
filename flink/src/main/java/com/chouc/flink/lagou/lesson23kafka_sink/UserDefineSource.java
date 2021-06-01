package com.chouc.flink.lagou.lesson23kafka_sink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author chouc
 * @version V1.0
 * @Title: UserDefineSource
 * @Package com.chouc.flink.lagou.lession23kafka_sink
 * @Description:
 * @date 7/30/20
 */
public class UserDefineSource implements SourceFunction<String> {
    private boolean isRunning = true;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        for (int i = 0; i < 24; i++) {
            String a = "cjl,15,chengdu,1"+i+",2020-11-05 23:40:"+(20+i)+","+(1604590800+i);
            String b = "mxb,15,chengdu,2"+i+",2020-11-05 23:40:"+(20+i)+","+(1604590800+i);
            ctx.collect(a);
            ctx.collect(b);
            System.out.println("1");
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
