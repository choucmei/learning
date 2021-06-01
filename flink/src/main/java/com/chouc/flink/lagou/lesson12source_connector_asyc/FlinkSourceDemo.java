package com.chouc.flink.lagou.lesson12source_connector_asyc;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkSourceDemo
 * @Package com.chouc.flink.lagou.lession12source_connector
 * @Description:
 * @date 2020/9/21
 */
public class FlinkSourceDemo {
    static class Person{

    }


    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();

        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.createLocalEnvironment();

        // read text file from local files system
        DataSet<String> localDs = env.readTextFile("file:///path/to/my/textfile");

        // read text file from an HDFS running at nnHost:nnPort
        DataSet<String> hdfsDs = env.readTextFile("hdfs://nnHost:nnPort/path/to/my/textfile");

        // read a CSV file with three fields
        DataSet<Tuple3<Integer,String,Double>> csvDs = env.readCsvFile("file:///path/to/my/textfile.csv")
                .types(Integer.class, String.class, Double.class);

        // read a CSV file with five fields, taking only two of them
        DataSet<Tuple2<String,Double>> csvDs2 = env.readCsvFile("file:///path/to/my/textfile.csv")
                // take the first and the fourth field
                .includeFields("10010")
                .types(String.class, Double.class);

        // read a CSV file with three fields into a POJO (Person.class) with corresponding fields
        DataSet<Person> csvDs3 = env.readCsvFile("file:///path/to/my/textfile.csv")
                .pojoType(Person.class, "name", "age", "zipcode");



        DataSet<String> text = env.fromElements(
                "Flink Spark Storm",
                "Flink Flink Flink",
                "Spark Spark Spark",
                "Storm Storm Storm"
        );


        List data = new ArrayList<Tuple3<Integer,Integer,Integer>>();
        data.add(new Tuple3<>(0,1,0));
        data.add(new Tuple3<>(0,1,1));
        data.add(new Tuple3<>(0,2,2));
        data.add(new Tuple3<>(0,1,3));
        data.add(new Tuple3<>(1,2,5));
        data.add(new Tuple3<>(1,2,9));
        data.add(new Tuple3<>(1,2,11));
        data.add(new Tuple3<>(1,2,13));
        DataSet<Tuple3<Integer,Integer,Integer>> items = env.fromCollection(data);


        DataStream<String> sDs = sEnv.socketTextStream("localhost",8888);
        sEnv.addSource(new SocketTextStreamFunction("localhost",8888,"\n",3));

        sEnv.addSource(new SourceFunction<String>(){
            private boolean isRunning = true;

            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                while (isRunning){
                    ctx.collect("data");
                }
            }

            @Override
            public void cancel() {
                isRunning=false;
            }
        });

    }
}
