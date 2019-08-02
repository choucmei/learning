package chouc.spark.demo.interview;


import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;


public class JDBCdataScoure {

    public static void main(String[] args) {

        SparkSession spark = new SparkSession.
                Builder().
                appName("JDBCDataSource").
                master("local").
                getOrCreate();

        Map<String, String> options = new HashMap<String,String>();
        options.put("url", "jdbc:mysql://20.0.20.203/testdb");
        options.put("dbtable", "student_infos");

        Dataset<Row> studentInfosDF = spark.
                read().
                format("jdbc").
                options(options).
                option("user", "root").
                option("password", "123456").
                load();
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "123456");
        options.put("dbtable", "student_scores");
        Dataset<Row> studentScoresDF = spark.
                read().
                format("jdbc").
                options(options).
                jdbc("jdbc:mysql://20.0.20.203/testdb", "student_scores", connectionProperties);


        JavaPairRDD<String, Tuple2<Integer, Integer>> studentsRDD =

                studentInfosDF.javaRDD().mapToPair(

                        new PairFunction<Row, String, Integer>() {


                            @Override
                            public Tuple2<String, Integer> call(Row row) throws Exception {
                                return new Tuple2<String, Integer>(row.getString(0),
                                        Integer.valueOf(String.valueOf(row.get(1))));
                            }

                        })
                        .join(studentScoresDF.javaRDD().mapToPair(

                                new PairFunction<Row, String, Integer>() {


                                    @Override
                                    public Tuple2<String, Integer> call(Row row) throws Exception {
                                        return new Tuple2<String, Integer>(String.valueOf(row.get(0)),
                                                Integer.valueOf(String.valueOf(row.get(1))));
                                    }

                                }));
        JavaRDD<Row> studentRowRdd = studentsRDD.map(new Function<Tuple2<String,Tuple2<Integer,Integer>>, Row>() {

            @Override
            public Row call(Tuple2<String, Tuple2<Integer, Integer>> tuple)
                    throws Exception {
                // TODO Auto-generated method stub
                return RowFactory.create(tuple._1,tuple._2._1,tuple._2._2);
            }
        });

        JavaRDD<Row> filteredStudentRowsRDD = studentRowRdd.filter(new Function<Row, Boolean>() {

            @Override
            public Boolean call(Row row) throws Exception {
                // TODO Auto-generated method stub
                if(row.getInt(2) > 80){
                    return true;
                }
                return false;
            }
        });

        List<org.apache.spark.sql.types.StructField> structFields = new ArrayList<org.apache.spark.sql.types.StructField>();
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
        structFields.add(DataTypes.createStructField("score", DataTypes.IntegerType, true));
        StructType structType = DataTypes.createStructType(structFields);

        Dataset<Row> studentsDF = spark.createDataFrame(filteredStudentRowsRDD, structType);

        Row[] rows = (Row[]) studentsDF.collect();

        for(Row row : rows){
            System.out.println(row);
        }

        studentsDF.javaRDD().foreach(new VoidFunction<Row>() {

            @Override
            public void call(Row row) throws Exception {
                // TODO Auto-generated method stub
                String sql = "insert into good_student_infos values("
                        + "'" + String.valueOf(row.getString(0)) + "',"
                        + Integer.valueOf(String.valueOf(row.get(1))) + ","
                        + Integer.valueOf(String.valueOf(row.get(2))) + ")";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = null;
                Statement stmt = null;

                try {
                    conn = DriverManager.getConnection("jdbc:mysql://20.0.20.203:3306/testdb", "root", "root");
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally{
                    if(stmt != null){
                        stmt.close();
                    }
                    if(conn != null){
                        conn.close();
                    }
                }
            }
        });
        spark.close();

    }
}
