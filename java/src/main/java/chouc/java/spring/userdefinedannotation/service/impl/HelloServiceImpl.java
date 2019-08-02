package chouc.java.spring.userdefinedannotation.service.impl;


import chouc.java.spring.userdefinedannotation.annotation.RpcService;
import chouc.java.spring.userdefinedannotation.service.HelloService;

@RpcService("HelloServicebb")
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return "Hello! " + name;
    }
    
    public void test(){
    	System.out.println("test");
    }
}
