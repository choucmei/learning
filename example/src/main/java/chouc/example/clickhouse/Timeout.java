package chouc.example.clickhouse;

import com.google.common.collect.Maps;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.response.ClickHouseResultSet;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: Timeout
 * @Package chouc.example.clickhouse
 * @Description:
 * @date 8/6/19
 */
public class Timeout {
    public static void main(String[] args) throws SQLException {
        ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        clickHouseProperties.setSocketTimeout(1000000);
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource("jdbc:clickhouse://slave01:8123", clickHouseProperties);
        Connection connection = clickHouseDataSource.getConnection();
        Statement statement = connection.createStatement();
        ClickHouseResultSet result = (ClickHouseResultSet) statement.executeQuery("desc query_log");
        LinkedHashMap<String, String> list = Maps.newLinkedHashMap();
        while (result.next()) {
            list.put(result.getString(1),result.getString(2)+":");
        }
        System.out.println(list.size());
        System.out.println(list);
//        result.bis
////        System.out.println(result);

    }
}
