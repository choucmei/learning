package com.chouc.flink.table.demo1;


import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.ExplainDetail;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkTableDemo01
 * @Package com.chouc.flink.table
 * @Description:
 * @date 2021/4/6
 */
public class FlinkTableDemo01 {
    public static void main(String[] args) {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment tableEnvironment = BatchTableEnvironment.create(environment);

        DataSource<WC> source = environment.fromElements(new WC("hello",1),new WC("hello",1),new WC("world",1));

        tableEnvironment.createTemporaryView("wc",source);
        Table selectedTable = tableEnvironment.sqlQuery("select * from wc");
        selectedTable.printSchema();
        selectedTable.execute().print();
        System.out.println(selectedTable.explain(ExplainDetail.CHANGELOG_MODE));
        System.out.println(selectedTable.explain(ExplainDetail.ESTIMATED_COST));

    }
}
