package com.chouc.flink.lagou.lesson19dimemsion_join;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author chouc
 * @version V1.0
 * @Title: DimemsionSyncRealtime
 * @Package com.chouc.flink.lagou.lession19dim_sync
 * @Description:
 * @date 2020/10/9
 */
public class FlinkDimemsionJoinSync {
    class DimemsionSyncRealtimeMap extends RichMapFunction {
        private Connection conn = null;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dim?characterEncoding=UTF-8", "admin", "admin");
        }

        @Override
        public Object map(Object value) throws Exception {
            PreparedStatement pst = conn.prepareStatement("select city_name from info where city_id = ?");
            // query
            return null;
        }

        @Override
        public void close() throws Exception {
            super.close();
            conn.close();
        }
    }
}
