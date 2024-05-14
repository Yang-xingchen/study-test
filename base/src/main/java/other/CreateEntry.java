package other;

import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateEntry {

    /**
     * new 关键字
     * 使用构造函数: true
     */
    @Test
    public void byNew() {
        int old = Entry.getUseConstructorCount();
        Entry entry = new Entry();
        assertEquals(old + 1, Entry.getUseConstructorCount());
    }

    /**
     * 复制
     * 使用构造函数: false
     * 需实体类实现{@link Cloneable}
     */
    @Test
    public void byClone() throws Throwable {
        Entry src = new Entry();
        int old = Entry.getUseConstructorCount();
        Entry clone = (Entry) src.clone();
        assertEquals(old, Entry.getUseConstructorCount());
    }

    /**
     * 反序列化
     * 使用构造函数: false
     * 需实体类实现{@link Serializable}
     */
    @Test
    public void bySerialization() throws Throwable {
        Entry src = new Entry();
        byte[] bytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(os)){
            outputStream.writeObject(src);
            bytes = os.toByteArray();
        }
        int old = Entry.getUseConstructorCount();
        Entry serialization;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(is)) {
            serialization = (Entry) inputStream.readObject();
        }
        assertEquals(old, Entry.getUseConstructorCount());
    }

    /**
     * 反射
     * 使用构造函数: true
     */
    @Test
    public void byReflect() throws Throwable {
        int old = Entry.getUseConstructorCount();
        Entry entry = Entry.class.getConstructor().newInstance();
        assertEquals(old + 1, Entry.getUseConstructorCount());
    }

    /**
     * 方法句柄
     * 使用构造函数: true
     */
    @Test
    public void byMethodHandles() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle constructor = lookup.findConstructor(Entry.class, MethodType.methodType(void.class));
        int old = Entry.getUseConstructorCount();
        Entry entry = (Entry) constructor.invokeExact();
        assertEquals(old + 1, Entry.getUseConstructorCount());
    }

    /**
     * 直接分配内存
     * 使用构造函数: false
     */
    @Test
    public void byUnsafe() throws Throwable {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        int old = Entry.getUseConstructorCount();
        Entry entry = (Entry) unsafe.allocateInstance(Entry.class);
        assertEquals(old, Entry.getUseConstructorCount());
    }

}
