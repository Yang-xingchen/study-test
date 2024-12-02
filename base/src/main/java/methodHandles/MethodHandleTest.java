package methodHandles;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {

    @Test
    public void testDifferentMethodName() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        // 方法类型，第一个参数为返回值，后续参数为方法参数
        MethodType methodType = MethodType.methodType(void.class);
        B b = new B();
        // 通过实例、方法名、方法类型查找MethodHandel并通过invokeExact调用
        // 同实例、方法类型不同方法名
        // b.print()
        lookup.findVirtual(B.class, "print", methodType).invokeExact(b);
        // b.print2()
        lookup.findVirtual(B.class, "print2", methodType).invokeExact(b);
    }

    @Test
    public void testDifferentClass() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class);
        // 同方法名、方法类型不同实例(无继承关系)
        // a.print()
        lookup.findVirtual(A.class, "print", methodType).invokeExact(new A());
        // b.print()
        lookup.findVirtual(B.class, "print", methodType).invokeExact(new B());
    }

    @Test
    public void testChangeArg() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class);
        B b = new B();
        // b.print()
        lookup.findVirtual(B.class, "print", methodType).invokeExact(b);
        // 补充参数
        MethodType methodType2 = methodType.appendParameterTypes(String.class);
        // b.print("Something...")
        lookup.findVirtual(B.class, "print", methodType2).invokeExact(b, "Something...");
    }

    @Test
    public void testBind() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, String.class);
        B b = new B();
        // 绑定实例
        MethodHandle bind = lookup.bind(b, "print", methodType);
        // b.print("Something...")
        bind.invokeExact("Something...");
        // b.print("Something2...")
        bind.invokeExact("Something2...");
    }

    @Test
    public void testBindTo() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, String.class, String.class);
        MethodHandle handle = lookup.findVirtual(B.class, "print", methodType);
        // 绑定参数，按顺序绑定
        MethodHandle bind = handle.bindTo(new B()).bindTo("value1");
        // b.print("value1", "value2")
        bind.invokeExact("value2");
    }

    @Test
    public void testBindTo2() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, String.class, String.class);
        // 静态方法
        MethodHandle handle = lookup.findStatic(B.class, "staticPrint", methodType);
        MethodHandle bind = handle.bindTo("value1");
        // B.print("value1", "value2")
        bind.invokeExact("value2");
    }

    @Test
    public void testInterface() throws Throwable {
        Impl impl = new Impl();
        // 接口lookup
        MethodHandles.Lookup lookup = impl.interfaceLookup();
        // 接口默认方法，interface test: methodHandles.Impl
        lookup.findVirtual(Impl.class, "test", MethodType.methodType(void.class)).invokeExact(impl);
        // 实例复写方法，impl test2:class methodHandles.Impl
        lookup.findVirtual(Impl.class, "test2", MethodType.methodType(void.class)).invokeExact(impl);
        try {
            // 实例private方法，无法执行
            lookup.findVirtual(Impl.class, "test3", MethodType.methodType(void.class)).invokeExact(impl);
        } catch (IllegalAccessException e) {
            System.err.println("interface lookup impl private method:" + e.getMessage());
        }
        // 实例lookup，都有权限
        lookup = impl.implLookup();
        lookup.findVirtual(Impl.class, "test", MethodType.methodType(void.class)).invokeExact(impl);
        lookup.findVirtual(Impl.class, "test2", MethodType.methodType(void.class)).invokeExact(impl);
        lookup.findVirtual(Impl.class, "test3", MethodType.methodType(void.class)).invokeExact(impl);
    }

    @Test
    public void testTestLookup() {
        // 只能执行public方法
        MethodHandles.Lookup testLookup = MethodHandles.lookup();
        testStatic(testLookup);
        testNoStatic(testLookup);
    }

    @Test
    public void testALookup() {
        // 所有方法均可执行
        MethodHandles.Lookup aLookup = A.getLookup();
        testStatic(aLookup);
        testNoStatic(aLookup);
    }

    @Test
    public void testPrivateLookupIn() throws IllegalAccessException {
        // 只能执行public方法
        MethodHandles.Lookup aLookup = MethodHandles.privateLookupIn(MethodHandleTest.class, MethodHandles.lookup());
        testStatic(aLookup);
        testNoStatic(aLookup);
    }

    @Test
    public void testPrivateLookupInA() throws IllegalAccessException {
        // 所有方法均可执行
        MethodHandles.Lookup aLookup = MethodHandles.privateLookupIn(A.class, MethodHandles.lookup());
        testStatic(aLookup);
        testNoStatic(aLookup);
    }

    public void testStatic(MethodHandles.Lookup lookup) {
        try {
            MethodType noArgNoReturnMethodType = MethodType.methodType(void.class);
            lookup.findStatic(A.class, "noArgNoReturnStaticPrivate", noArgNoReturnMethodType).invokeExact();
        } catch (IllegalAccessException e) {
            System.err.println("noArgNoReturnStaticPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgHasReturnMethodType = MethodType.methodType(String.class);
            String str = (String) lookup.findStatic(A.class, "noArgHasReturnStaticPrivate", noArgHasReturnMethodType).invokeExact();
            System.out.println("返回:" + str);
        } catch (IllegalAccessException e) {
            System.err.println("noArgHasReturnStaticPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType oneArgNoReturnMethodType = MethodType.methodType(void.class, String.class);
            lookup.findStatic(A.class, "oneArgNoReturnStaticPrivate", oneArgNoReturnMethodType).invokeExact("Something...");
        } catch (IllegalAccessException e) {
            System.err.println("oneArgNoReturnStaticPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgNoReturnMethodType = MethodType.methodType(void.class);
            lookup.findStatic(A.class, "noArgNoReturnStaticPublic", noArgNoReturnMethodType).invokeExact();
        } catch (IllegalAccessException e) {
            System.err.println("noArgNoReturnStaticPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgHasReturnMethodType = MethodType.methodType(String.class);
            String str = (String) lookup.findStatic(A.class, "noArgHasReturnStaticPublic", noArgHasReturnMethodType).invokeExact();
            System.out.println("返回:" + str);
        } catch (IllegalAccessException e) {
            System.err.println("noArgHasReturnStaticPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType oneArgNoReturnMethodType = MethodType.methodType(void.class, String.class);
            lookup.findStatic(A.class, "oneArgNoReturnStaticPublic", oneArgNoReturnMethodType).invokeExact("Something...");
        } catch (IllegalAccessException e) {
            System.err.println("oneArgNoReturnStaticPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void testNoStatic(MethodHandles.Lookup lookup) {
        A a = new A();
        try {
            MethodType noArgNoReturnMethodType = MethodType.methodType(void.class);
            lookup.findVirtual(A.class, "noArgNoReturnPrivate", noArgNoReturnMethodType).invokeExact(a);
        } catch (IllegalAccessException e) {
            System.err.println("noArgNoReturnPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgHasReturnMethodType = MethodType.methodType(String.class);
            String str = (String) lookup.findVirtual(A.class, "noArgHasReturnPrivate", noArgHasReturnMethodType).invokeExact(a);
            System.out.println("返回:" + str);
        } catch (IllegalAccessException e) {
            System.err.println("noArgHasReturnPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType oneArgNoReturnMethodType = MethodType.methodType(void.class, String.class);
            lookup.findVirtual(A.class, "oneArgNoReturnPrivate", oneArgNoReturnMethodType).invokeExact(a, "Something...");
        } catch (IllegalAccessException e) {
            System.err.println("oneArgNoReturnPrivate: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgNoReturnMethodType = MethodType.methodType(void.class);
            lookup.findVirtual(A.class, "noArgNoReturnPublic", noArgNoReturnMethodType).invokeExact(a);
        } catch (IllegalAccessException e) {
            System.err.println("noArgNoReturnPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType noArgHasReturnMethodType = MethodType.methodType(String.class);
            String str = (String) lookup.findVirtual(A.class, "noArgHasReturnPublic", noArgHasReturnMethodType).invokeExact(a);
            System.out.println("返回:" + str);
        } catch (IllegalAccessException e) {
            System.err.println("noArgHasReturnPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            MethodType oneArgNoReturnMethodType = MethodType.methodType(void.class, String.class);
            lookup.findVirtual(A.class, "oneArgNoReturnPublic", oneArgNoReturnMethodType).invokeExact(a, "Something...");
        } catch (IllegalAccessException e) {
            System.err.println("oneArgNoReturnPublic: "+e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
