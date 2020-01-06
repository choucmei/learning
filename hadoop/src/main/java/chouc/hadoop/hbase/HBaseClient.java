package chouc.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;

/**
 * @author chouc
 * @version V1.0
 * @Title: HBaseClient
 * @Package chouc.hadoop.hbase
 * @Description:
 * @date 11/26/19
 */
public class HBaseClient {
    /**
     * 配置ss
     */
    static Configuration configuration = null;
    private Connection connection = null;
    private Admin admin = null;
    private Table table = null;

    private String constantTableName = "KYLIN_D7STGSZC8P";


    @Before
    public void init() throws Exception {
        configuration = HBaseConfiguration.create();// 配置
        configuration.set("hbase.zookeeper.quorum", "192.168.90.97");// zookeeper地址
        configuration.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
        table = connection.getTable(TableName.valueOf(constantTableName));
    }


    @Test
    public void getInfo() throws IOException {
        Scan scan = new Scan();
        PageFilter filter = new PageFilter(10);
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        System.out.println("**************");
        for (Result result:scanner){
            System.out.println("ROWKEY:"+new String(result.getRow()));

            result.getFamilyMap("F1".getBytes()).forEach((kb,vb)->{
                System.out.println("F1:"+new String(kb)+":"+new String(vb));
            });

            result.getFamilyMap("F2".getBytes()).forEach((kb,vb)->{
                System.out.println("F2:"+new String(kb)+":"+new String(vb));
            });

            result.getFamilyMap("F3".getBytes()).forEach((kb,vb)->{
                System.out.println("F3:"+new String(kb)+":"+new String(vb));
            });
        }
    }

}
