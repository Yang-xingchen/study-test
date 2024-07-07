package other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 破坏单例
 * <pre>
 *                      enum    checkCaller
 *     reflect          F       F
 *     MethodHandle     T       F
 *     Serialization    F       T
 *     Unsafe           T       T
 * </pre>
 */
public class BreakSingleton {

    @BeforeAll
    public static void setup() {
        // 确保已加载单例
        System.out.println(ByCallerCheck.INSTANT);
    }

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
        MethodHandle constructor = MethodHandles.lookup().findConstructor(ByEnum.class, MethodType.methodType(void.class, String.class, int.class));
        ByEnum instant = (ByEnum) constructor.invokeExact("INSTANT", 0);
        Assertions.assertNotSame(ByEnum.INSTANT, instant);

        Assertions.assertEquals(1, ByEnum.values().length);
    }

    /**
     * 枚举法 vs 序列化
     * 破坏失败
     */
    @Test
    public void enumVsSerialization() throws Throwable {
        Assertions.assertEquals(ByEnum.INSTANT, serialization(ByEnum.INSTANT));
    }

    /**
     * 枚举法 vs Unsafe
     * 破坏成功
     */
    @Test
    public void enumVsUnsafe() throws Exception {
        ByEnum instance = (ByEnum) getUnsafe().allocateInstance(ByEnum.class);
        Assertions.assertNotSame(ByEnum.INSTANT, instance);

        Assertions.assertEquals(1, ByEnum.values().length);
    }

    /**
     * 枚举法单例
     */
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
        MethodHandle constructor = MethodHandles.lookup().findConstructor(ByCallerCheck.class, MethodType.methodType(void.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ByCallerCheck instanct = (ByCallerCheck) constructor.invokeExact();
        });
    }

    /**
     * 检查调用者 vs 序列化
     * 破坏成功
     */
    @Test
    public void checkVsSerialization() throws Throwable {
        Assertions.assertNotEquals(ByCallerCheck.INSTANT, serialization(ByCallerCheck.INSTANT));
    }

    /**
     * 检查调用者 vs Unsafe
     * 破坏成功
     */
    @Test
    public void checkVsUnsafe() throws Exception {
        ByCallerCheck instance = (ByCallerCheck) getUnsafe().allocateInstance(ByCallerCheck.class);
        Assertions.assertNotSame(ByCallerCheck.INSTANT, instance);
    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }

    private static <T> T serialization(T src) throws IOException, ClassNotFoundException {
        byte[] bytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(os)){
            outputStream.writeObject(src);
            bytes = os.toByteArray();
        }
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(is)) {
            return  (T) inputStream.readObject();
        }
    }

    /**
     * 检查调用者
     * 于构造函数中检查调用者为当前类
     */
    static class ByCallerCheck implements Serializable {

        static ByCallerCheck INSTANT = new ByCallerCheck();

        private ByCallerCheck() {
            StackTraceElement[] stackTraceElements = Thread.getAllStackTraces().get(Thread.currentThread());
            if (!stackTraceElements[3].getClassName().contains(ByCallerCheck.class.getSimpleName())) {
                throw new IllegalArgumentException();
            }
        }

    }

}
