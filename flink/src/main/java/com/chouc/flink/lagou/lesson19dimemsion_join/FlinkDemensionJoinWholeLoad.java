package com.chouc.flink.lagou.lesson19dimemsion_join;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: WholeLoad
 * @Package com.chouc.flink.lagou.lession19dimemsion_join
 * @Description:
 * @date 2020/10/9
 */
public class FlinkDemensionJoinWholeLoad {
    class WholeLoadMap extends RichMapFunction {
        ScheduledExecutorService executor = null;
        private Map<String,String> cache;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        load();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },5,5, TimeUnit.MINUTES);
        }

        private void load() throws ClassNotFoundException, SQLException {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dim?characterEncoding=UTF-8", "root", "123456");
            PreparedStatement statement = con.prepareStatement("sql ");
            // cache.put(key, querySet);
            con.close();
        }

        @Override
        public Object map(Object value) throws Exception {
            return null;
        }
    }
}
