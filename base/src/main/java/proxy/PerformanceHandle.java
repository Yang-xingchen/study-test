package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PerformanceHandle implements InvocationHandler {


	private Object target;

	public PerformanceHandle(Object target) {
		this.target = target;
	}

	public Object getTarget() {
		return target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("getTarget".equals(method.getName())) {
			return target;
		}
		System.out.println("start");
		System.out.println(proxy.getClass().getName() + " " + method.getName());
		Object obj = method.invoke(target, args);
		System.out.println("end");
		return obj;
	}

}
