package invoke;

import java.lang.invoke.MethodHandles;

public interface Interface {

    default MethodHandles.Lookup interfaceLookup() {
        return MethodHandles.lookup();
    }

    default void test() {
        System.out.println("interface test: " + getClass().getName());
    }

    default void test2() {
        System.out.println("interface test2: " + getClass().getName());
    }

}
