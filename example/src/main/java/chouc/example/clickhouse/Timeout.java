package chouc.example.clickhouse;

import org.json.JSONObject;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.ClickHouseStatement;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource("jdbc:clickhouse://l:8123", clickHouseProperties);
        Connection connection = clickHouseDataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select mycase when sum(ua.exposure_pv) = 0 then 0 else round(sum(ua.click_pv) * 1.0/sum(ua.exposure_pv),4) end as rate_click, sum(ua.exposure_uv) as exposure_uv, sum(ua.click_pv) as click_pv, sum(ua.exposure_pv) as exposure_pv, sum(ua.click_uv) as click_uv from ((select uniqExactMerge(uv) as click_uv,sumMerge(cnt) as click_pv,0 as exposure_uv,0 as exposure_pv from ad.terminalclick_log_day where week >= 20190701 and week <= 20190805 and mediaid = '69712365a1e645a592ac6974d1a7c2ce') union all (select 0 as click_uv,0 as click_pv,uniqExactMerge(uv) as exposure_uv,sumMerge(cnt) as exposure_pv from ad.impression_log_day where week >= 20190701 and week <= 20190805 and mediaid = '69712365a1e645a592ac6974d1a7c2ce')) as ua");
        System.out.println(result);

    }
}
