package chou.component.zookeeper.case01;

import org.apache.zookeeper.*;

import static org.apache.zookeeper.ZooDefs.OpCode.create;

public class DistributedServer {

    private ZooKeeper zooKeeper = null;
    private static final String connectString="10.211.55.6:2181,10.211.55.7:2181,10.211.55.8:2181";
    private static final int sessionTimeout = 2000;
    private static final String parentNode="/servers";

    public void getConnect()throws Exception{
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

    /**
     * 判断父节点是否存在
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean checkNodeExist(String path) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path,true)==null?false:true;
    }

    /**
     * 注册到zookeeper
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void registerServer(String hostName) throws KeeperException, InterruptedException {
        String create = zooKeeper.create(parentNode+"/server",hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostName + " is opnline.."+ create);

    }

    //业务
    public void handleBussiness(String hostName) throws InterruptedException {
        System.out.println(hostName + "staring working......");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) {
        DistributedServer server = new DistributedServer();
        try {
            server.getConnect();
            server.registerServer(args[0]);
            server.handleBussiness(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
