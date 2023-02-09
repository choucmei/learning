package com.chouc.flink.test;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Arrays;

/**
 * @Author choucmei
 * @create 2022/5/31
 */
public class Test {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode().build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        // 模拟输入
        DataStream<Tuple3<String, Long, Long>> tuple3DataStream =
                env.fromCollection(Arrays.asList(
                        Tuple3.of("2", 1L, 1627218000000L),
                        Tuple3.of("2", 101L, 1627218000000L + 6000L),
                        Tuple3.of("2", 201L, 1627218000000L + 7000L),
                        Tuple3.of("2", 301L, 1627218000000L + 7000L),
                        Tuple3.of("4", 401L, 1627218000000L + 7000L))

                );
        // 分桶取模 udf
        tEnv.createTemporarySystemFunction("mod", new Mod());

        // 中文映射 udf
        tEnv.createTemporarySystemFunction("status_mapper", new StatusMapperUDF());

        Schema schema = Schema.newBuilder()
                .columnByExpression("status", "f0")
                .columnByExpression("id", "f1")
                .columnByExpression("timestamp", "f2")
                .build();

        tEnv.createTemporaryView("source_db.source_table", tuple3DataStream, schema);


        String sql = "WITH detail_tmp AS (\n"
                + "  SELECT\n"
                + "    status,\n"
                + "    id,\n"
                + "    `timestamp`\n"
                + "  FROM\n"
                + "    (\n"
                + "      SELECT\n"
                + "        status,\n"
                + "        id,\n"
                + "        `timestamp`,\n"
                + "        row_number() over(\n"
                + "          PARTITION by id\n"
                + "          ORDER BY\n"
                + "            `timestamp` DESC\n"
                + "        ) AS rn\n"
                + "      FROM source_db.source_table"
                + "    )\n"
                + "  WHERE\n"
                + "    rn = 1\n"
                + ")\n"
                + "SELECT\n"
                + "  DIM.status_new as status,\n"
                + "  sum(part_uv) as uv\n"
                + "FROM\n"
                + "  (\n"
                + "    SELECT\n"
                + "      status,\n"
                + "      count(distinct id) as part_uv\n"
                + "    FROM\n"
                + "      detail_tmp\n"
                + "    GROUP BY\n"
                + "      status,\n"
                + "      mod(id, 100)\n"
                + "  )\n"
                + "LEFT JOIN LATERAL TABLE(status_mapper(status)) AS DIM(status_new) ON TRUE\n"
                + "GROUP BY\n"
                + "  DIM.status_new";

        Table result = tEnv.sqlQuery(sql);

        result.execute().print();

//        env.execute();
    }
}
