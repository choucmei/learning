package chou.component.zookeeper.case01;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class DistributedClient {
    private ZooKeeper zooKeeper = null;
    private static final String connectString="10.211.55.6:2181,10.211.55.7:2181,10.211.55.8:2181";
    private static final int sessionTimeout = 2000;
    private static final String parentNode="/servers";
    //注意：加 volatile的意义
    private volatile List<String> serverlist;

    public void getConnect()throws Exception{
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //收到时间通知的回调函数
                System.out.println(watchedEvent.getType() + "---" + watchedEvent.getPath());

                try {
                    zooKeeper.getChildren("/server/",true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    getServerList();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //参数1：要创建的节点路径 参数2：节点数据 参数3：节点的权限 参数4：节点的类型
    }

    // 获取服务器列表
    public void getServerList() throws KeeperException, InterruptedException {
        List<String> list = zooKeeper.getChildren(parentNode,true);
        List<String> servers = new ArrayList<String>();
        for (String child : list){
            servers.add(new String(zooKeeper.getData(parentNode+"/"+child,false,null)));
        }
        serverlist = servers;
        System.out.println(serverlist);
    }

    //业务
    public void handleBussiness() throws InterruptedException {
        System.out.println( "client staring working......");
        Thread.sleep(Long.MAX_VALUE);
    }


    public static void main(String[] args) throws Exception {
        DistributedClient distributedClient =new DistributedClient();
        distributedClient.getConnect();
        distributedClient.getServerList();
        distributedClient.handleBussiness();
    }

}
