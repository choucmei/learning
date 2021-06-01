package com.chouc.flink.lagou.lesson02wordcount_table;


import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.table.types.DataType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.flink.table.api.Expressions.row;

/**
 * @author chouc
 * @version V1.0
 * @Title: TableWordcount
 * @Package com.chouc.flink.lagou.lession02wordcount_table
 * @Description:
 * @date 2020/8/18
 */
public class TableWordcount {

    public static class EntityWC {
        public String word;
        public Integer count;

        public EntityWC() {
        }

        public EntityWC(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public Integer getCount() {
            return count;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "EntityWC{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }


    public static void main(String[] args) {
        ExecutionEnvironment executionEnvironment = ExecutionEnvironment.createLocalEnvironment();
        BatchTableEnvironment tEnv = BatchTableEnvironment.create(executionEnvironment);
        String words = "mm cc zz zz";
        List<ApiExpression> list = new ArrayList<>();
        for (String s : words.split(" ")) {
            list.add(row(s, 1));
        }

        DataType dataType = DataTypes.ROW(
                DataTypes.FIELD("word", DataTypes.VARCHAR(10).notNull()),
                DataTypes.FIELD("count_value", DataTypes.BIGINT().notNull())
        );

        Table table = tEnv.fromValues(dataType, list);
        table.printSchema();
        tEnv.createTemporaryView("WordCount", table);
        TableResult rsT = tEnv.sqlQuery("select word as word,sum(count_value) as wc from WordCount group by word").execute();
        rsT.print();

    }
}
