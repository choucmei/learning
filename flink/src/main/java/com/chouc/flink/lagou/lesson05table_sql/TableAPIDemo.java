package com.chouc.flink.lagou.lesson05table_sql;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author chouc
 * @version V1.0
 * @Title: TableAPIDemo
 * @Package com.chouc.flink.lagou.lession05table_sql
 * @Description:
 * @date 2020/8/23
 */

public class TableAPIDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        DataStreamSource<EntityItem> source = env.addSource(new UserDefineItemSource());
//        DataStream<EntityItem> evenDataStream = source.split(new OutputSelector<EntityItem>() {
//            @Override
//            public Iterable<String> select(EntityItem value) {
//                List<String> output = new ArrayList<>();
//                if (value.getId() % 2 == 0) {
//                    output.add("even");
//                } else {
//                    output.add("odd");
//                }
//                return output;
//            }
//        }).select("even");
//        DataStream<EntityItem> oddDataStream = source.split(new OutputSelector<EntityItem>() {
//            @Override
//            public Iterable<String> select(EntityItem value) {
//                List<String> output = new ArrayList<>();
//                if (value.getId() % 2 == 0) {
//                    output.add("even");
//                } else {
//                    output.add("odd");
//                }
//                return output;
//            }
//        }).select("odd");
//        tableEnv.createTemporaryView("evenTable", evenDataStream, $("id"), $("name"));
//        tableEnv.createTemporaryView("oddTable", oddDataStream, $("id"), $("name"));
//        Table queryTable = tableEnv.sqlQuery("select a.id as fId,a.name as fName,b.id as sId,b.name as sName from evenTable as a join oddTable as b on a.name = b.name");
//        queryTable.printSchema();
//        TypeInformation<Tuple4<Integer, String, Integer, String>> info = TypeInformation.of(new TypeHint<Tuple4<Integer, String, Integer, String>>() {
//        });
////        tableEnv.toRetractStream(queryTable, EntityJoinItem.class).print();
//        tableEnv.toRetractStream(queryTable, info).print();
//        env.execute("streaming sql job");
    }
}

class UserDefineItemSource implements SourceFunction<EntityItem> {
    boolean cancel = false;

    @Override
    public void run(SourceContext<EntityItem> ctx) throws Exception {
        while (true) {
            EntityItem entityItem = generate();
            ctx.collect(entityItem);
        }
    }

    @Override
    public void cancel() {
        cancel = true;
    }

    private EntityItem generate() {
        int i = new Random().nextInt(100);
        ArrayList<String> list = new ArrayList();
        list.add("HAT");
        list.add("TIE");
        list.add("SHOE");
        EntityItem item = new EntityItem();
        item.setName(list.get(new Random().nextInt(3)));
        item.setId(i);
        return item;
    }
}

