package methodHandles;

import java.lang.invoke.MethodHandles;

public class A {

    public static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }

    private static void noArgNoReturnStaticPrivate() {
        System.out.println("调用无参数无返回值静态私有方法");
    }

    private static void oneArgNoReturnStaticPrivate(String value) {
        System.out.println("调用参数为"+value+"无返回值静态私有方法");
    }

    private static String noArgHasReturnStaticPrivate() {
        System.out.println("调用无参数有返回值静态私有方法");
        return "Something...";
    }

    public static void noArgNoReturnStaticPublic() {
        System.out.println("调用无参数无返回值静态公开方法");
    }

    public static void oneArgNoReturnStaticPublic(String value) {
        System.out.println("调用参数为"+value+"无返回值静态公开方法");
    }

    public static String noArgHasReturnStaticPublic() {
        System.out.println("调用无参数有返回值静态公开方法");
        return "Something...";
    }

    private void noArgNoReturnPrivate() {
        System.out.println("调用无参数无返回值私有方法");
    }

    private void oneArgNoReturnPrivate(String value) {
        System.out.println("调用参数为"+value+"无返回值私有方法");
    }

    private String noArgHasReturnPrivate() {
        System.out.println("调用无参数有返回值私有方法");
        return "Something...";
    }

    public void noArgNoReturnPublic() {
        System.out.println("调用无参数无返回值公开方法");
    }

    public void oneArgNoReturnPublic(String value) {
        System.out.println("调用参数为"+value+"无返回值公开方法");
    }

    public String noArgHasReturnPublic() {
        System.out.println("调用无参数有返回值公开方法");
        return "Something...";
    }

    public void print() {
        System.out.println("A 输出");
    }

}
