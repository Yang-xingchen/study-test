package temp;

import java.util.Arrays;
import java.util.stream.Stream;

public class Arg {

    public static void main(String[] args) throws Throwable{
        Stream.of(Arg.class.getDeclaredMethods()).forEach(System.out::println);
        Stream.of(Arg.class.getDeclaredMethods()).map(method -> Arrays.toString(method.getParameterTypes())).forEach(System.err::println);
        System.out.println(String[].class);
        Class<?> clazz = Arg.class.getDeclaredMethod("test", String[].class).getReturnType();
        System.err.println("void==: "+(clazz==void.class));
        System.err.println("void equals: "+(void.class.equals(clazz)));
        System.err.println("void: "+clazz);
        System.err.println("voidName: "+clazz.getName());
        System.err.println("voidMethod: "+Arrays.toString(clazz.getDeclaredMethods()));
        System.err.println("voidField: "+Arrays.toString(clazz.getFields()));
        Object ret = Arg.class.getDeclaredMethod("test", Object[].class).invoke(null, new Object[]{null});
        System.out.println("ret:" + ret);
//        System.out.println("retClass:" + ret.getClass());   //java.lang.NullPointerException
    }

    public static void test(String...arg){

    }

    public static void test(Object[] arg){

    }

}
