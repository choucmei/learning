package chouc.java.socket;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ServiceProxy {

    public static Object create(Class<?> interfaceClass){
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass.getClass()}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("localhost",8080);
                OutputStream outputStream = socket.getOutputStream();
                TransBeanTest<Integer> transBean = null;
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(transBean);
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                transBean = (TransBeanTest) objectInputStream.readObject();
                objectOutputStream.close();
                objectInputStream.close();
                socket.close();
                System.out.println("result:"+transBean.getResult());
                return method.invoke(interfaceClass.newInstance(),args);
            }
        });
    }



}
