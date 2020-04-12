package methodHandles;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {

    @Test
    public void testDifferentMethodName() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class);
        B b = new B();
        lookup.findVirtual(B.class, "print", methodType).invokeExact(b);
        lookup.findVirtual(B.class, "print2", methodType).invokeExact(b);
    }

    @Test
    public void testDifferentClass() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class);
        lookup.findVirtual(A.class, "print", methodType).invokeExact(new A());
        lookup.findVirtual(B.class, "print", methodType).invokeExact(new B());
    }

    @Test
    public void testChangeArg() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class);
        B b = new B();
        lookup.findVirtual(B.class, "print", methodType).invokeExact(b);
        MethodType methodType2 = methodType.appendParameterTypes(String.class);
        lookup.findVirtual(B.class, "print", methodType2).invokeExact(b, "Something...");
    }

    @Test
    public void testBind() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, String.class);
        B b = new B();
        MethodHandle bind = lookup.bind(b, "print", methodType);
        bind.invokeExact("Something...");
        bind.invokeExact("Something2...");
    }

    @Test
    public void testTestLookup() {
        MethodHandles.Lookup testLookup = MethodHandles.lookup();
        testStatic(testLookup);
        testNoStatic(testLookup);
    }

    @Test
    public void testALookup() {
        MethodHandles.Lookup aLookup = A.getLookup();
        testStatic(aLookup);
        testNoStatic(aLookup);
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

}
