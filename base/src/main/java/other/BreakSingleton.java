package other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 破坏单例
 * <pre>
 *                  enum  checkCaller
 *     reflect      F       F
 *     MethodHandle T       F
 *     Unsafe       T       T
 * </pre>
 */
public class BreakSingleton {

    /**
     * 枚举法 vs 反射
     * 破坏失败
     */
    @Test
    public void enumVsReflect() throws Throwable {
        Constructor<ByEnum> constructor = ByEnum.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> constructor.newInstance("INSTANT", 0));
    }

    /**
     * 枚举法 vs 方法句柄
     * 破坏成功
     */
    @Test
    public void enumVsMethodHandle() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle constructor = lookup.findConstructor(ByEnum.class, MethodType.methodType(void.class, String.class, int.class));
        ByEnum instanct = (ByEnum) constructor.invokeExact("INSTANT", 0);
        Assertions.assertFalse(instanct == ByEnum.INSTANT);
    }

    /**
     * 枚举法 vs Unsafe
     * 破坏成功
     */
    @Test
    public void enumVsUnsafe() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        ByEnum instance = (ByEnum) unsafe.allocateInstance(ByEnum.class);
        Assertions.assertFalse(instance == ByEnum.INSTANT);
    }

    enum ByEnum {
        INSTANT
    }

    /**
     * 测试检查调用者
     */
    @Test
    public void check() {
        Assertions.assertThrows(IllegalArgumentException.class, ByCallerCheck::new);
    }

    /**
     * 检查调用者 vs 反射
     * 破坏失败
     */
    @Test
    public void checkVsReflect() {
        System.out.println(ByCallerCheck.INSTANT);
        try {
            Constructor<ByCallerCheck> constructor = ByCallerCheck.class.getDeclaredConstructor();
            constructor.newInstance();
        } catch (Exception e) {
            Assertions.assertEquals(InvocationTargetException.class, e.getClass());
            Assertions.assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    /**
     * 检查调用者 vs 方法句柄
     * 破坏失败
     */
    @Test
    public void checkVsMethodHandle() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle constructor = lookup.findConstructor(ByCallerCheck.class, MethodType.methodType(void.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ByCallerCheck instanct = (ByCallerCheck) constructor.invokeExact();
        });
    }

    /**
     * 检查调用者 vs Unsafe
     * 破坏成功
     */
    @Test
    public void checkVsUnsafe() throws Exception {
        System.out.println(ByCallerCheck.INSTANT);
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        ByCallerCheck instance = (ByCallerCheck) unsafe.allocateInstance(ByCallerCheck.class);
        Assertions.assertFalse(instance == ByCallerCheck.INSTANT);
    }

    static class ByCallerCheck {
        static ByCallerCheck INSTANT = new ByCallerCheck();

        private ByCallerCheck() {
            StackTraceElement[] stackTraceElements = Thread.getAllStackTraces().get(Thread.currentThread());
            if (!stackTraceElements[3].getClassName().contains("ByCallerCheck")) {
                throw new IllegalArgumentException();
            }
        }

    }

}
