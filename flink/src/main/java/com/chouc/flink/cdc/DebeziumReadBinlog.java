package com.chouc.flink.cdc;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.spi.OffsetCommitPolicy;

public class DebeziumReadBinlog {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("database.server.name", "mysql_binlog_source");
        properties.setProperty("offset.flush.interval.ms", "9223372036854775807");
        properties.setProperty("database.history", "com.ververica.cdc.debezium.internal.FlinkDatabaseSchemaHistory");
        properties.setProperty("database.history.instance.name", "65f9d730-b3f0-4a7d-9078-0de939acf2d0");
        properties.setProperty("database.hostname", "s4");
        properties.setProperty("bigint.unsigned.handling.mode", "precise");
        properties.setProperty("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore");
        properties.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        properties.setProperty("database.history.skip.unparseable.ddl", "true");
        properties.setProperty("database.password", "123456");
        properties.setProperty("include.schema.changes", "false");
        properties.setProperty("name", "engine");
        properties.setProperty("database.port", "3306");
        properties.setProperty("tombstones.on.delete", "false");
        properties.setProperty("snapshot.mode", "initial");
        properties.setProperty("database.user", "root");
        properties.setProperty("database.whitelist", "test");

        DebeziumEngine<?> engine =
            DebeziumEngine.create(Connect.class)
                .using(properties)
                .notifying(event -> {
                    System.out.println("*****");
                    System.out.println(event);
                })
                .using(OffsetCommitPolicy.always())
                .using(
                    (success, message, error) -> {
                        if (success) {
                            // Close the handover and prepare to exit.
                            System.out.println("sucess " + message);
                        } else {
                            System.out.println("error " + error + " " + message);
                        }
                    })
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
    }
}
