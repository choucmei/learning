package chou.component.zookeeper.simple;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class SimpleZkClient {
    private static final String connectString="s2:2181,s3:2181,s1:2181";
    //private static final String connectString="10.211.55.6:2181,10.211.55.7:2181,10.211.55.8:2181";
    private static final int sessionTimeout = 2000;
    ZooKeeper zooKeeper = null;

    @Before
    public void init() throws IOException {

        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //收到时间通知的回调函数
                System.out.println(watchedEvent.getType() + "---" + watchedEvent.getPath());

                try {
                    zooKeeper.getChildren("/",true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //参数1：要创建的节点路径 参数2：节点数据 参数3：节点的权限 参数4：节点的类型


    }

    @Test
    public void create() throws KeeperException, InterruptedException {
        zooKeeper.create("/eclipse","hellozk".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void testExist()throws Exception{
        Stat stat = zooKeeper.exists("/eclipse",true);
        System.out.println(stat);
    }

    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> list = zooKeeper.getChildren("/",true);
        for (String child:list){
            System.out.println(" child : "+child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void getData() throws Exception{
        byte[] data = zooKeeper.getData("/eclipse",true,null);
        System.out.println(new String(data));
    }


    public void deleteZnode()throws Exception{
        //参数2：指定要删除的版本，-1表示删除所有
        zooKeeper.delete("/eclipse",-1);

    }

}
