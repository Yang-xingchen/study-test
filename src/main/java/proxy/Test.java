package proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;


public class Test {

    private static final Logger LOGGER = LogManager.getLogger(Test.class);

	public static void main(String[] args){
		ForumService target = new ForumServiceImpl();
		PerformanceHandle handle = new PerformanceHandle(target);
		ForumService proxy = (ForumService)Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handle);
		proxy.removeForum(12);
		proxy.removeTopic(21);
		LOGGER.info(proxy.getClass().getName());

        LOGGER.info("--------------------------------------------------------------------------");

        Stream.of(proxy.getClass().getMethods()).forEach(LOGGER::info);

        LOGGER.info("--------------------------------------------------------------------------");

        LOGGER.info(((PerformanceHandle)Proxy.getInvocationHandler(proxy)).getTarget().getClass().getName());

        LOGGER.info("--------------------------------------------------------------------------");

        LOGGER.info(((ForumServiceImpl)((PerformanceHandle)Proxy.getInvocationHandler(proxy)).getTarget()).test());
        LOGGER.info(target.toString());
    }
}
