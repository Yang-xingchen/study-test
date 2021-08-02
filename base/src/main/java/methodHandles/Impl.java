package methodHandles;

import java.lang.invoke.MethodHandles;

public class Impl implements Interface{

    public MethodHandles.Lookup implLookup() {
        return MethodHandles.lookup();
    }

    @Override
    public void test2() {
        System.out.println("impl test2:" + getClass());
    }

    private void test3() {
        System.out.println("impl test3: " + getClass());
    }
}
