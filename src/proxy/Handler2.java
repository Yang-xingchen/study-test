package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Handler2 implements InvocationHandler {

    private Object target;

    public Handler2() {
    }

    public Object getTarget() {

        return target;
    }

    public Handler2 setTarget(Object target) {
        this.target = target;
        return this;
    }

    public Handler2(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.err.println(method+Arrays.toString(args));
        System.err.println(proxy.getClass());
        return method.invoke(target, args);
    }
}
