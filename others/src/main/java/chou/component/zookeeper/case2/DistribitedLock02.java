package chou.component.zookeeper.case2;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DistribitedLock02 {

    private ZooKeeper zooKeeper = null;
    private static final String connectString="10.211.55.6:2181,10.211.55.7:2181,10.211.55.8:2181";
    private static final int sessionTimeout = 2000;
    private static final String parentNode="/lock";
    // 记录自己创建的子节点路径
    private volatile String thisPath;

    private String prefixPath = "/lock_";

    private boolean haveLock = false;

    private String serverName;

    public DistribitedLock02(String serverName) {
        this.serverName = serverName;
    }

    public void getConnect()throws Exception{
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                //收到时间通知的回调函数


                if (watchedEvent.getType()== Event.EventType.NodeChildrenChanged && watchedEvent.getPath().equals(parentNode)){

                    try {
                        List<String> chidrenList = zooKeeper.getChildren(parentNode,true);
                        Collections.sort(chidrenList);
                        String thisNode = thisPath.substring((parentNode + "/").length());
                        if (chidrenList.indexOf(thisNode) == 0){
                            handleBusiness();
                            thisPath = zooKeeper.create(parentNode+prefixPath, serverName.getBytes()  , ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                        }
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //参数1：要创建的节点路径 参数2：节点数据 参数3：节点的权限 参数4：节点的类型
    }


    public void startWorking() throws KeeperException, InterruptedException {
        thisPath = zooKeeper.create(parentNode+prefixPath, serverName.getBytes()  , ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        List<String> chidrenList = zooKeeper.getChildren(parentNode,true);
        if (chidrenList.size() == 1){
            handleBusiness();
            thisPath = zooKeeper.create(parentNode+prefixPath, serverName.getBytes()  , ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        }
    }

    //作业
    public void handleBusiness() throws KeeperException, InterruptedException {
        System.out.println( new String(zooKeeper.getData(thisPath,false,null))+" get lock "+thisPath);
        zooKeeper.delete(thisPath,-1);
        Thread.sleep(new Random().nextInt(1000));
    }

    public static void main(String[] args) throws Exception {
        DistribitedLock02 distribitedLock1 = new DistribitedLock02("server 02");
        distribitedLock1.getConnect();
        distribitedLock1.startWorking();
        Thread.sleep(Long.MAX_VALUE);

    }

}
