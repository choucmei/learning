package chouc.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseClientTest {
    /**
     * 配置ss
     */
    static Configuration configuration = null;
    private Connection connection = null;
    private Admin admin = null;
    private Table table = null;

    private String constantTableName="user";


    @Before
    public void init() throws Exception {
        configuration = HBaseConfiguration.create();// 配置
        configuration.set("hbase.zookeeper.quorum", "s1,s2,s3");// zookeeper地址
        configuration.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
        table = connection.getTable(TableName.valueOf(constantTableName));
    }

    @After
    public void destroy() throws IOException {
        admin.close();
        table.close();
        connection.close();
    }

    /**
     * 创建一个表
     *
     * @throws Exception
     */
    @Test
    public void createTable() throws Exception {
        // 创建表管理类

        // 创建表描述类
        TableName tableName = TableName.valueOf("user03"); // 表名称
        if (admin.tableExists(tableName)){
            System.out.printf(" the table does exists");
            return;
        }
        HTableDescriptor desc = new HTableDescriptor(tableName);
        // 创建列族的描述类
        HColumnDescriptor family = new HColumnDescriptor("info"); // 列族
        // 将列族添加到表中
        desc.addFamily(family);
        HColumnDescriptor family2 = new HColumnDescriptor("info2"); // 列族
        // 将列族添加到表中
        desc.addFamily(family2);
        // 创建表
        admin.createTable(desc); // 创建表
    }

    @Test
    public void deleteTable() throws IOException {
        TableName tableName = TableName.valueOf("user02"); // 表名称
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }

    @Test
    public void insertData() throws IOException {
        // 设置row-key
        Put data = new Put(Bytes.toBytes("100001"));
        // 设置row-family-rowname-value
        data.addColumn(Bytes.toBytes("info01"),Bytes.toBytes("name"),Bytes.toBytes("mxb"));
        data.addColumn(Bytes.toBytes("info01"),Bytes.toBytes("age"),Bytes.toBytes("18"));
        data.addColumn(Bytes.toBytes("info01"),Bytes.toBytes("sex"),Bytes.toBytes("nan"));
        data.addColumn(Bytes.toBytes("info01"),Bytes.toBytes("addr"),Bytes.toBytes("anhui"));
        table.put(data);
    }


    @Test
    public void insertDatas() throws IOException {
        // 设置row-key
        Put data2 = new Put(Bytes.toBytes("100007"));
        Put data1 = new Put(Bytes.toBytes("100006"));
        // 设置row-family-rowname-value
        data2.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("name"),Bytes.toBytes("mxb"));
        data2.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("age"),Bytes.toBytes("18"));
        data2.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("sex"),Bytes.toBytes("nan"));
        data2.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("addr"),Bytes.toBytes("anhui"));
        data1.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("name"),Bytes.toBytes("zmtb"));
        data1.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("age"),Bytes.toBytes("18"));
        data1.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("sex"),Bytes.toBytes("nan"));
        data1.addColumn(Bytes.toBytes("info02"),Bytes.toBytes("addr"),Bytes.toBytes("anhui"));
        List<Put> list = new ArrayList<Put>();
        list.add(data1);
        list.add(data2);
        table.put(list);
    }

    @Test
    public void deleteData() throws IOException {
        //row key
        Delete data = new Delete(Bytes.toBytes("100001"));
        data.addColumn(Bytes.toBytes("info01"),Bytes.toBytes("name"));
        table.delete(data);
    }

    @Test
    public void queryData() throws IOException {
        Get get = new Get(Bytes.toBytes("100002"));
        Result r = table.get(get);
        byte[] name = r.getValue(Bytes.toBytes("info01"),Bytes.toBytes("name"));
        System.out.println("name:"+Bytes.toString(name));
    }

    @Test
    public void scanData() throws IOException {
        Scan scan = new Scan();
        scan.withStartRow(Bytes.toBytes("100001"));
        scan.withStopRow(Bytes.toBytes("100001"));
//        scan.addFamily(Bytes.toBytes("info02"));
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result:resultScanner){
            System.out.println(Bytes.toString(result.getRow()));
            byte[] name = result.getValue(Bytes.toBytes("info01"),Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info01"),Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info01"),Bytes.toBytes("age"));
            byte[] addr = result.getValue(Bytes.toBytes("info01"),Bytes.toBytes("addr"));
            System.out.print("name:"+Bytes.toString(name)+",");
            System.out.print("sex:"+Bytes.toString(sex)+",");
            System.out.print("age:"+Bytes.toString(age)+",");
            System.out.println("addr:"+Bytes.toString(addr)+",");
        }
    }


    @Test
    public void scanDataBySingleFilter() throws IOException {
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes("info02"),Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes("cjl"));
        Scan scan = new Scan();
        scan.setFilter(singleColumnValueFilter);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result:resultScanner){
            System.out.println(Bytes.toString(result.getRow()));
            byte[] name = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("age"));
            byte[] addr = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("addr"));
            System.out.print("name:"+Bytes.toString(name)+",");
            System.out.print("sex:"+Bytes.toString(sex)+",");
            System.out.print("age:"+Bytes.toString(age)+",");
            System.out.println("addr:"+Bytes.toString(addr)+",");
        }
    }


    /**
     * 根据 row-key 的前缀来匹配
     * @throws IOException
     */
    @Test
    public void scanDataByPreFixFilter() throws IOException {
        PrefixFilter prefixFilter = new PrefixFilter(Bytes.toBytes("1000"));
        Scan scan = new Scan();
        scan.setFilter(prefixFilter);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result:resultScanner){
            System.out.println(Bytes.toString(result.getRow()));
            byte[] name = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("age"));
            byte[] addr = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("addr"));
            System.out.print("name:"+Bytes.toString(name)+",");
            System.out.print("sex:"+Bytes.toString(sex)+",");
            System.out.print("age:"+Bytes.toString(age)+",");
            System.out.println("addr:"+Bytes.toString(addr)+"。");
        }
    }


    /**
     * 根据 row-key 的来匹配
     * @throws IOException
     */
    @Test
    public void scanDataByPreRowFilter() throws IOException {
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER, new BinaryComparator(Bytes.toBytes("100001")));
        Scan scan = new Scan();
        scan.setFilter(rowFilter);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            System.out.println(Bytes.toString(result.getRow()));
            byte[] name = result.getValue(Bytes.toBytes("info02"), Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info02"), Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info02"), Bytes.toBytes("age"));
            byte[] addr = result.getValue(Bytes.toBytes("info02"), Bytes.toBytes("addr"));
            System.out.print("name:" + Bytes.toString(name) + ",");
            System.out.print("sex:" + Bytes.toString(sex) + ",");
            System.out.print("age:" + Bytes.toString(age) + ",");
            System.out.println("addr:" + Bytes.toString(addr) + "。");
        }
    }

    /**
     * 根据 多个过滤器去过滤
     * @throws IOException
     */
    @Test
    public void scanDataByFilterList() throws IOException {
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER,new BinaryComparator(Bytes.toBytes("100001")));
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes("info02"),Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes("mxb"));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(rowFilter);
        filterList.addFilter(singleColumnValueFilter);
        Scan scan = new Scan();
        scan.setFilter(filterList);
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result:resultScanner){
            System.out.println(Bytes.toString(result.getRow()));
            byte[] name = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("age"));
            byte[] addr = result.getValue(Bytes.toBytes("info02"),Bytes.toBytes("addr"));
            System.out.print("name:"+Bytes.toString(name)+",");
            System.out.print("sex:"+Bytes.toString(sex)+",");
            System.out.print("age:"+Bytes.toString(age)+",");
            System.out.println("addr:"+Bytes.toString(addr)+"。");
        }
    }


}
