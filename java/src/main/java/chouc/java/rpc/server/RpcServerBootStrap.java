package chouc.java.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author chouc
 * @version V1.0
 * @Title: RpcServerBootStrap
 * @Package chouc.java.rpc.server
 * @Description:
 * @date 7/31/19
 */
public class RpcServerBootStrap {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("rpc-server/spring.xml");
    }
}
