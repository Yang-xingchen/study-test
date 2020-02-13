package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Handler1 implements InvocationHandler {

    private Object target;

    public Handler1() {
    }

    public Object getTarget() {

        return target;
    }

    public Handler1 setTarget(Object target) {
        this.target = target;
        return this;
    }

    public Handler1(Object target) {

        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method+Arrays.toString(args));
        return method.invoke(target, args);
    }
}
