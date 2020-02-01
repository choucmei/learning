package chouc.example.clickhouse;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chouc
 * @version V1.0
 * @Title: ParseSql
 * @Package chouc.example.clickhouse
 * @Description:
 * @date 1/8/20
 */
public class ParseSql {
    public static void main(String[] args) throws JSQLParserException {
        String a = "CREATE EXTERNAL TABLE `impression_log`(\n" +
                "  `did` string,\n" +
                "  `mac` string,\n" +
                "  `devicetype` string,\n" +
                "  `os` string,\n" +
                "  `osv` string,\n" +
                "  `connectiontype` tinyint,\n" +
                "  `ip` string,\n" +
                "  `appver` string,\n" +
                "  `region` string,\n" +
                "  `lat` double,\n" +
                "  `lon` double,\n" +
                "  `userid` string,\n" +
                "  `vip` tinyint,\n" +
                "  `server_time` bigint,\n" +
                "  `sessionid` string,\n" +
                "  `flowid` string,\n" +
                "  `scheduleid` string,\n" +
                "  `mediaid` string,\n" +
                "  `adtotal` int,\n" +
                "  `adpos` int,\n" +
                "  `dspid` string,\n" +
                "  `aid` string,\n" +
                "  `category` string,\n" +
                "  `price` double,\n" +
                "  `advertiserid` string,\n" +
                "  `creativeid` string,\n" +
                "  `series` string,\n" +
                "  `episode` int,\n" +
                "  `title` string,\n" +
                "  `channelid` string,\n" +
                "  `reporttype` string,\n" +
                "  `asseturl` string,\n" +
                "  `contentplayhead` string,\n" +
                "  `httpmethod` string,\n" +
                "  `advertisingid` string,\n" +
                "  `gender` string,\n" +
                "  `agegroup` string,\n" +
                "  `contentcategory` string,\n" +
                "  `contentlabel` string)\n" +
                "PARTITIONED BY (\n" +
                "  `day` int,\n" +
                "  `minute` string)\n" +
                "STORED AS PARQUET;";
        System.out.println(getTableNameBySql(a));
    }

    /**
     * 查询字段
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    private static List<String> test_select_items(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectitems = plain.getSelectItems();
        List<String> str_items = new ArrayList<String>();
        if (selectitems != null) {
            for (SelectItem selectitem : selectitems) {
                str_items.add(selectitem.toString());
            }
        }
        return str_items;
    }

    public static List getTableNameBySql(String sql) throws JSQLParserException {
        CCJSqlParserManager parser=new CCJSqlParserManager();
        List columnList = null;
        Statement stmt=parser.parse(new StringReader(sql));
        if (stmt instanceof CreateTable) {
            columnList = ((CreateTable) stmt).getColumnDefinitions();
        }
        return columnList;
    }

}
