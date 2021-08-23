package com.chouc.flink.table;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Over;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.table.types.AtomicDataType;
import org.apache.flink.table.types.logical.TimestampKind;
import org.apache.flink.table.types.logical.TimestampType;

import static org.apache.flink.table.api.Expressions.*;

// https://ci.apache.org/projects/flink/flink-docs-release-1.13/zh/docs/dev/table/tableapi/#scan-projection-and-filter

public class FlinkTableApi {
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment);

        Table source = tableEnv.fromValues(DataTypes.ROW(
                DataTypes.FIELD("a", DataTypes.STRING()), DataTypes.FIELD("b", DataTypes.STRING()), DataTypes.FIELD("c", DataTypes.STRING()), DataTypes.FIELD("rt", new AtomicDataType(new TimestampType(true, TimestampKind.ROWTIME, 3)))),
                row("4", "0", "1 2", "2021-08-12 15:04:50.269"),
                row("1", "1", "1 2", "2021-08-12 15:04:51.556"),
                row("2", "0", "2 2", "2021-08-12 15:04:52.756"),
                row("3", "0", "3 2", "2021-08-12 15:04:53.956"),
                row("5", "0", "1 2", "2021-08-12 15:04:54.148"),
                row("8", "1", "3 2", "2021-08-12 15:04:55.355"),
                row("9", "1", "4 2", "2021-08-12 15:04:56.500"),
                row("7", "1", "2 2", "2021-08-12 15:04:57.716"),
                row("6", "1", "1 2", "2021-08-12 15:04:58.900"),
                row("10", "1", "1 1", "2021-08-12 15:04:59.076"),
                row("11", "0", "2 1", "2021-08-12 15:05:12.444"),
                row("12", "0", "3 1", "2021-08-12 15:05:32.827"),
                row("13", "0", "4 1", "2021-08-12 15:05:43.043"),
                row("14", "1", "5 1", "2021-08-12 15:05:02.259"),
                row("15", "0", "1 1", "2021-08-12 15:05:53.228"),
                row("16", "0", "2 1", "2021-08-12 15:05:22.653"),
                row("17", "1", "3 1", "2021-08-12 15:06:13.612"),
                row("18", "1", "4 1", "2021-08-12 15:06:44.179"),
                row("19", "0", "5 1", "2021-08-12 15:06:03.428"),
                row("20", "1", "1 1", "2021-08-12 15:06:33.996"),
                row("21", "1", "2 1", "2021-08-12 15:06:54.372"),
                row("22", "1", "3 1", "2021-08-12 15:06:23.803"),
                row("23", "1", "4 1", "2021-08-12 15:07:04.564"),
                row("24", "0", "5 1", "2021-08-12 15:07:35.140"),
                row("25", "0", "1 1", "2021-08-12 15:07:14.764"),
                row("26", "0", "2 1", "2021-08-12 15:07:45.324"),
                row("27", "0", "3 1", "2021-08-12 15:07:55.516"),
                row("28", "0", "4 1", "2021-08-12 15:07:24.955"),
                row("29", "0", "5 1", "2021-08-12 15:08:05.692"),
                row("30", "1", "6 1", "2021-08-12 15:08:15.875"),
                row("31", "1", "1", "2021-08-12 15:08:16.875"));

        SingleOutputStreamOperator<Tuple4<String, String, String, Long>> dataStreamSource = executionEnvironment.fromElements(
                Tuple4.of("4", "0", "1 2", 1629637071165L),
                Tuple4.of("1", "1", "1 2", 1629637072165L),
                Tuple4.of("2", "0", "2 2", 1629637073165L),
                Tuple4.of("3", "0", "3 2", 1629637074165L),
                Tuple4.of("5", "0", "1 2", 1629637075165L),
                Tuple4.of("8", "1", "3 2", 1629637075165L),
                Tuple4.of("9", "1", "4 2", 1629637075165L),
                Tuple4.of("7", "1", "2 2", 1629637075165L),
                Tuple4.of("6", "1", "1 2", 1629637075165L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple4<String, String, String, Long>>noWatermarks().withTimestampAssigner(new SerializableTimestampAssigner<Tuple4<String, String, String, Long>>() {

                    @Override
                    public long extractTimestamp(Tuple4<String, String, String, Long> element, long recordTimestamp) {
                        return element.f3;
                    }
                }));

        Table orders2 = tableEnv.fromDataStream(dataStreamSource, $("a"), $("b"), $("c"), $("f3").rowtime())
                .renameColumns($("f3").as("rt"));


        tableEnv.createTemporaryView("Orders", source);
        Table orders = tableEnv.from("Orders");


//        Table ratesHistory = tableEnv.fromValues(DataTypes.ROW(
//                DataTypes.FIELD("r_b", DataTypes.STRING()), DataTypes.FIELD("o", DataTypes.STRING())),
//                row("0", "^^^"),
//                row("1", "&&&")
//        );


        /**
         * Column Operations #
         */

        // from values

        Table table1 = tableEnv.fromValues(row(1L, "ABC"), row(2L, "ABCD"));
        table1.execute().print();

        Table table2 = tableEnv.fromValues(DataTypes.ROW(DataTypes.FIELD("id", DataTypes.DECIMAL(10, 2)), DataTypes.FIELD("name", DataTypes.STRING())), row(1L, "ABC"), row(2L, "ABCD"));
        table2.execute().print();

        // select
        Table select1 = orders.select($("a"), $("c").as("d"));
        Table select2 = orders.select($("*"));
        select1.execute().print();
        select2.execute().print();

        // as
        Table as = orders.as("x, y, z, t");
        as.execute().print();


        // where and filter
        Table where = orders.where($("b").isEqual("red"));
        where.execute().print();

        Table filter = orders.filter($("b").isEqual("1"));
        filter.execute().print();

        // add column
        Table addColumns = orders.addColumns(concat($("c"), "sunny"));
        addColumns.execute().print();

        // drop columns
        Table dropColumns = orders.dropColumns($("b"));
        dropColumns.execute().print();

        Table renameColumns = orders.renameColumns($("a").as("a_new"));
        renameColumns.execute().print();


        /**
         * Aggregations
         */
        // GroupBy Aggregation
        Table aggr = orders.groupBy($("a")).select($("a"), $("b").count().as("c"));
        aggr.execute().print();

        // GroupBy Window Aggregation
        Table winAggr = orders
                .window(Tumble.over(lit(5).seconds()).on($("rt")).as("w"))
                .groupBy($("b"), $("w"))
                .select($("b"), $("w").start().as("start"), $("w").end().as("end"), $("b").count().as("w_c"));
        winAggr.execute().print();

        orders.addOrReplaceColumns($("a").cast(DataTypes.BIGINT()).as("a")).printSchema();


        // Over Window Aggregation  BUG
        Table overWin = orders2.addOrReplaceColumns($("a").cast(DataTypes.BIGINT()).as("a")).window(Over
                .partitionBy($("b"))
                .orderBy($("rt"))
                .preceding(UNBOUNDED_RANGE)
                .following(CURRENT_RANGE)
                .as("w"))
                .select($("b"), $("a").max().over($("w")), $("a").min().over($("w")), $("a").avg().over($("w")));
        overWin.execute().print();


        // Distinct aggregation on group by
        Table distinctAggr = orders.addOrReplaceColumns($("c").cast(DataTypes.BIGINT()).as("c")).groupBy($("b"))
                .select($("b"), $("c").sum().distinct());
        distinctAggr.execute().print();

        // Distinct aggregation on time window group by
        Table groupByWindowDistinctResult = orders.addOrReplaceColumns($("c").cast(DataTypes.BIGINT()).as("c"))
                .window(Tumble.over(lit(5).seconds()).on($("rt")).as("w"))
                .groupBy($("b"), $("w"))
                .select($("b"), $("w").start().as("start"), $("w").end().as("end"), $("c").sum().distinct());
        groupByWindowDistinctResult.execute().print();


        // Distinct aggregation on over window
        Table result = orders
                .window(Over
                        .partitionBy($("a"))
                        .orderBy($("rowtime"))
                        .preceding(UNBOUNDED_RANGE)
                        .as("w"))
                .select(
                        $("a"), $("b").avg().distinct().over($("w")),
                        $("b").max().over($("w")),
                        $("b").min().over($("w"))
                );
        result.execute().print();
        /**
         * Joins
         */

        Table left = tableEnv.fromDataStream(executionEnvironment.fromElements(
                Tuple3.of("xm1s", "1", "199"),
                Tuple3.of("hw", "2", "399"),
                Tuple3.of("op", "3", "99"),
                Tuple3.of("vi", "5", "99")))
                .as("a", "b", "c");
        Table right = tableEnv.fromDataStream(executionEnvironment.fromElements(
                Tuple3.of("1", "1111", "!!!!"),
                Tuple3.of("2", "2222", "@@@@"),
                Tuple3.of("3", "3333", "####"),
                Tuple3.of("4", "4444", "$$$$")))
                .as("d", "e", "f");

        Table innerJoin = left.join(right).where($("b").isEqual($("d")));
        innerJoin.execute().print();

        Table leftOuterJoin = left.leftOuterJoin(right, $("b").isEqual($("d")));
        leftOuterJoin.execute().print();

        Table rightOuterJoin = left.rightOuterJoin(right, $("b").isEqual($("d")));
        rightOuterJoin.execute().print();

        Table fullOuterJoin = left.fullOuterJoin(right, $("b").isEqual($("d")));
        fullOuterJoin.execute().print();

        // joinLateral
        TableFunction<Tuple2<String, String>> split = new FlinkTableUserTableFunction();
        tableEnv.createTemporarySystemFunction("split", split);

        Table joinUDF = orders
                .joinLateral(call("split", $("c")).as("s", "t"))
                .select($("a"), $("c"), $("s"), $("t"));
        joinUDF.execute().print();


        Table joinOutUDF = orders
                .leftOuterJoinLateral(call("split", $("c")).as("s", "t"))
                .select($("a"), $("c"), $("s"), $("t"));
        joinOutUDF.execute().print();


        //TemporalTableFunction others = ratesHistory.createTemporalTableFunction(
        //        $("r_b"),
        //        $("o"));
        //tableEnv.createTemporarySystemFunction("others",others);
        //Table table = orders2.joinLateral(call("others", $("rt")), $("b").isEqual($("r_b")));
        //table.execute().print();

        /**
         * Set Operations
         */
        //  The UNION operation on two unbounded tables is currently not supported.
        //Table unionTable = orders.union(orders2);
        //Table unionAlTable = orders.unionAl(orders2);
        //Table intersectTable = orders.intersect(orders2);
        //Table intersectAllTable = orders.intersectAll(orders2);
        //Table intersectAllTable = orders.intersectAll(orders2);
        //Table minusTable = orders.minus(orders2);
        //Table minusAllTable = orders.minusAll(orders2);
        //Table minusAllTable = orders.minusAll(orders2);
        // work
        Table inTable = orders.select($("a"), $("b"), $("c")).where($("a").in(orders.select($("a"))));
        inTable.execute().print();


        /**
         * OrderBy, Offset & Fetch
         */
        Table orderTable = orders.orderBy($("a").asc());
        orders.execute().print();

        // skips the first 3 records and returns all following records from the sorted result
        // FETCH is missed, which on streaming table is not supported currently.
        //Table offsetTable = orders.orderBy($("a").asc()).offset(3);
        //offsetTable.execute().print();

        // returns the first 5 records from the sorted result
        Table fetchTable = orders.orderBy($("a").asc()).fetch(3);
        fetchTable.execute().print();

        // skips the first 10 records and returns the next 5 records from the sorted result
        Table offsetAndFetchTable = orders.orderBy($("a").asc()).offset(10).fetch(3);
        offsetAndFetchTable.execute().print();


        /**
         * Insert
         */
        //orders.executeInsert("OutOrders");


        /**
         * Group Windows
         *
         *
         * Table table = input
         *   .window([GroupWindow w].as("w"))  // define window with alias w
         *   .groupBy($("w"))  // group the table by window w
         *   .select($("b").sum());  // aggregate
         *
         * Table table = input
         *   .window([GroupWindow w].as("w"))  // define window with alias w
         *   .groupBy($("w"), $("a"))  // group the table by attribute a and window w
         *   .select($("a"), $("b").sum());  // aggregate
         *
         *
         * Table table = input
         *   .window([GroupWindow w].as("w"))  // define window with alias w
         *   .groupBy($("w"), $("a"))  // group the table by attribute a and window w
         *   .select($("a"), $("w").start(), $("w").end(), $("w").rowtime(), $("b").count());
         *
         * // Tumbling Event-time Window
         * .window(Tumble.over(lit(10).minutes()).on($("rowtime")).as("w"));
         *
         * // Tumbling Processing-time Window (assuming a processing-time attribute "proctime")
         * .window(Tumble.over(lit(10).minutes()).on($("proctime")).as("w"));
         *
         * // Tumbling Row-count Window (assuming a processing-time attribute "proctime")
         * .window(Tumble.over(rowInterval(10)).on($("proctime")).as("w"));
         *
         *
         * // Sliding Event-time Window
         * .window(Slide.over(lit(10).minutes())
         *             .every(lit(5).minutes())
         *             .on($("rowtime"))
         *             .as("w"));
         *
         * // Sliding Processing-time window (assuming a processing-time attribute "proctime")
         * .window(Slide.over(lit(10).minutes())
         *             .every(lit(5).minutes())
         *             .on($("proctime"))
         *             .as("w"));
         *
         * // Sliding Row-count window (assuming a processing-time attribute "proctime")
         * .window(Slide.over(rowInterval(10)).every(rowInterval(5)).on($("proctime")).as("w"));
         *
         *
         * // Session Event-time Window
         * .window(Session.withGap(lit(10).minutes()).on($("rowtime")).as("w"));
         *
         * // Session Processing-time Window (assuming a processing-time attribute "proctime")
         * .window(Session.withGap(lit(10).minutes()).on($("proctime")).as("w"));
         *
         */


        /**
         * Over Windows
         *
         * Table table = input
         *   .window([OverWindow w].as("w"))           // define over window with alias w
         *   .select($("a"), $("b").sum().over($("w")), $("c").min().over($("w"))); // aggregate over the over window w
         *
         *
         *   // Unbounded Event-time over window (assuming an event-time attribute "rowtime")
         * .window(Over.partitionBy($("a")).orderBy($("rowtime")).preceding(UNBOUNDED_RANGE).as("w"));
         *
         * // Unbounded Processing-time over window (assuming a processing-time attribute "proctime")
         * .window(Over.partitionBy($("a")).orderBy("proctime").preceding(UNBOUNDED_RANGE).as("w"));
         *
         * // Unbounded Event-time Row-count over window (assuming an event-time attribute "rowtime")
         * .window(Over.partitionBy($("a")).orderBy($("rowtime")).preceding(UNBOUNDED_ROW).as("w"));
         *
         * // Unbounded Processing-time Row-count over window (assuming a processing-time attribute "proctime")
         * .window(Over.partitionBy($("a")).orderBy($("proctime")).preceding(UNBOUNDED_ROW).as("w"));
         *
         *
         *
         *
         * // Bounded Event-time over window (assuming an event-time attribute "rowtime")
         * .window(Over.partitionBy($("a")).orderBy($("rowtime")).preceding(lit(1).minutes()).as("w"));
         *
         * // Bounded Processing-time over window (assuming a processing-time attribute "proctime")
         * .window(Over.partitionBy($("a")).orderBy($("proctime")).preceding(lit(1).minutes()).as("w"));
         *
         * // Bounded Event-time Row-count over window (assuming an event-time attribute "rowtime")
         * .window(Over.partitionBy($("a")).orderBy($("rowtime")).preceding(rowInterval(10)).as("w"));
         *
         * // Bounded Processing-time Row-count over window (assuming a processing-time attribute "proctime")
         * .window(Over.partitionBy($("a")).orderBy($("proctime")).preceding(rowInterval(10)).as("w"));
         */


        /**
         * Row-based Operations
         */
        // map
        Table userFunction = orders
                .map(call(FlinkTableUserScalarFunction.class, $("c"))).as("cc", "c_length");
        userFunction.execute().print();


        FlinkTableUserScalarFunction func = new FlinkTableUserScalarFunction();
        tableEnv.createTemporaryFunction("func", func);
        Table userFunction2 = orders
                .map(call("func", $("c"))).as("cc", "c_length");
        userFunction2.execute().print();

        tableEnv.createTemporaryFunction("func3", FlinkTableUserScalarFunction.class);
        Table userFunction3 = orders
                .map(call("func3", $("c"))).as("cc", "c_length");
        userFunction3.execute().print();

        // flatmap
        FlinkTableUserFlatMapFunction flatMapFunc = new FlinkTableUserFlatMapFunction();
        tableEnv.createTemporaryFunction("ufm", flatMapFunc);

        Table flatMapTable = orders.flatMap(call("ufm", $("c")));
        flatMapTable.execute().print();

        // Aggregate
        FlinkTableUserAggregateFunction userAggFunc = new FlinkTableUserAggregateFunction();
        tableEnv.createTemporaryFunction("UserAggFunc", userAggFunc);
        Table aggregateTable = orders
                .groupBy($("b"))
                .aggregate(call("UserAggFunc", $("a")))
                .select($("b"), $("TMP_0$min"), $("TMP_0$max"));
        aggregateTable.execute().print();

        // Group Window Aggregate

        Table windowAggTable = orders
                .window(Tumble.over(lit(5).seconds())
                        .on($("rt"))
                        .as("w")) // define window
                .groupBy($("b"), $("w")) // group by key and window
                .aggregate(call("UserAggFunc", $("a")).as("a_min", "a_max"))
                .select($("b"), $("a_min"), $("a_max"), $("w").start(), $("w").end());
        windowAggTable.execute().print();

        // FlatAggregate
        tableEnv.createTemporaryFunction("top2", new FlinkTableUserTableAggregateFunction());
        Table flatAggregateTable = orders.groupBy($("b"))
                .flatAggregate(call("top2", $("a")).as("a", "rank"))
                .select($("b"), $("a"), $("rank"));
        flatAggregateTable.execute().print();

    }


}