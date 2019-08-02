package chouc.java.socket;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransBean<T> implements Serializable{
    private String className;
    private String methodName;
    private Object[] args;
    private Class[] argsType;
    private T result;

    public TransBean(String className, String methodName, Object[] args, Class[] argsType) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
        this.argsType = argsType;
    }



    public TransBean invok(){
        try {
            Class targetClass = Class.forName(getClassName());
            Method method = targetClass.getMethod(getMethodName(),argsType);
            this.result = (T) method.invoke(targetClass.newInstance(),args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getArgsType() {
        return argsType;
    }

    public void setArgsType(Class[] argsType) {
        this.argsType = argsType;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
