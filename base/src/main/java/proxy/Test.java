package proxy;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args){
		ForumService target = new ForumServiceImpl();
		PerformanceHandle handle = new PerformanceHandle(target);
		ForumService proxy = (ForumService)Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handle);
		proxy.removeForum(12);
		proxy.removeTopic(21);
		System.out.println(proxy.getClass().getName());

        System.out.println("--------------------------------------------------------------------------");

        Stream.of(proxy.getClass().getMethods()).forEach(System.out::println);

        System.out.println("--------------------------------------------------------------------------");

        System.out.println(((PerformanceHandle)Proxy.getInvocationHandler(proxy)).getTarget().getClass().getName());

        System.out.println("--------------------------------------------------------------------------");

        System.out.println(((ForumServiceImpl)((PerformanceHandle)Proxy.getInvocationHandler(proxy)).getTarget()).test());
        System.out.println(target);
    }

}
