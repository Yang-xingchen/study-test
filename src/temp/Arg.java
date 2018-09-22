package temp;

import java.util.Arrays;
import java.util.stream.Stream;

public class Arg {

    public static void main(String[] args) {
        Stream.of(Arg.class.getDeclaredMethods()).forEach(System.out::println);
        Stream.of(Arg.class.getDeclaredMethods()).map(method -> Arrays.toString(method.getParameterTypes())).forEach(System.err::println);
        System.out.println(String[].class);
    }

    public static void test(String...arg){

    }

    public static void test(Object[] arg){

    }

}
