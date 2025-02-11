package proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class LoadTest {

    public static void main(String[] args) {
        Service service1 = new Service1("Service1");
        Service service1_1 = new Service1("Service1_1");
        Service service2 = new Service2(2);

        Handler1 handler1_1 = new Handler1(service1);
        Handler1 handler1_1_1 = new Handler1(service1_1);
        Handler1 handler1_2 = new Handler1(service2);
        Handler2 handler2_1 = new Handler2(service1);
        Handler2 handler2_2 = new Handler2(service2);

        System.out.println("Interface:" + Arrays.equals(Service1.class.getInterfaces(), Service2.class.getInterfaces()));
        System.out.println("ClassLoader:" + Objects.equals(Service1.class.getClassLoader(), Service2.class.getClassLoader()));

        // true
        Service impl1_1_1 = (Service) Proxy.newProxyInstance(Service1.class.getClassLoader(), Service1.class.getInterfaces(), handler1_1);
        Service impl1_1_2 = (Service) Proxy.newProxyInstance(Service1.class.getClassLoader(), Service1.class.getInterfaces(), handler1_1);
        System.out.println("重复生成:" + Objects.equals(impl1_1_1.getClass(), impl1_1_2.getClass()));

        // true
        Service impl1_1_1_1 = (Service) Proxy.newProxyInstance(Service1.class.getClassLoader(), Service1.class.getInterfaces(), handler1_1_1);
        System.out.println("同handler类不同handler的对象:" + Objects.equals(impl1_1_1.getClass(), impl1_1_1_1.getClass()));

        // true
        Service impl1_2_1 = (Service) Proxy.newProxyInstance(Service2.class.getClassLoader(), Service2.class.getInterfaces(), handler1_2);
        System.out.println("同handler类不同handler、target类:" + Objects.equals(impl1_1_1.getClass(), impl1_2_1.getClass()));

        // true
        Service impl2_1_1 = (Service) Proxy.newProxyInstance(Service1.class.getClassLoader(), Service1.class.getInterfaces(), handler2_1);
        System.out.println("同handler类不同handler类:" + Objects.equals(impl1_1_1.getClass(), impl2_1_1.getClass()));

        // true
        Service impl2_2_1 = (Service) Proxy.newProxyInstance(Service2.class.getClassLoader(), Service2.class.getInterfaces(), handler2_2);
        System.out.println("不同handler、target类:" + Objects.equals(impl1_1_1.getClass(), impl2_2_1.getClass()));

        System.out.println(impl1_1_1.getClass() + "--" + impl2_2_1.getClass());

        System.out.println("==============================");
        System.out.println("StackTrace:");
        impl2_2_1.print();

        Class clazz = impl1_1_1.getClass();
        System.out.println(clazz.getName());
        System.out.println("==============================");
        System.out.println(" -DeclaredMethod:");
        Stream.of(clazz.getDeclaredMethods()).forEach(System.out::println);
        System.out.println("==============================");
        System.out.println(" -Method:");
        Stream.of(clazz.getMethods()).forEach(System.out::println);
        System.out.println("==============================");
        System.out.println(" -DeclaredField:");
        Stream.of(clazz.getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                System.out.println(field + ":" + field.get(impl1_1_1));
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println(field);
            }
        });
        System.out.println("==============================");
        System.out.println(" -Field:");
        Stream.of(clazz.getFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                System.out.println(field + ":" + field.get(impl1_1_1));
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println(field);
            }
        });
        System.out.println("==============================");
        System.out.println(" -Interface:");
        Stream.of(clazz.getInterfaces()).forEach(System.out::println);
        System.out.println("==============================");
        System.out.println(" -SuperClass:");
        System.out.println(clazz.getSuperclass());
    }
}
