package proxy;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PerformanceHandle implements InvocationHandler {
	private static final Logger LOGGER = LogManager.getLogger(PerformanceHandle.class);
	
	private Object target;
	public PerformanceHandle(Object target) {
		this.target = target;
	}

    public Object getTarget() {
        return target;
    }

    @Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    if ("getTarget".equals(method.getName())){
	        return target;
        }
		LOGGER.info("start");
		LOGGER.info(proxy.getClass().getName()+" "+method.getName());
		Object obj = method.invoke(target, args);
		LOGGER.info("end");
		return obj;
	}

}
