package other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnsafeTest {

    private static final long INT_MEMORY = 4;

    @Test
    public void unsafeException() {
        Assertions.assertThrows(SecurityException.class, Unsafe::getUnsafe);
    }


    private Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unsafe() {
        getUnsafe();
    }

    @Test
    public void arr() {
        Unsafe unsafe = getUnsafe();
        long memory = unsafe.allocateMemory(INT_MEMORY * 10);
        unsafe.setMemory(memory, INT_MEMORY * 10, (byte) 0);
        System.out.println("memory: " + memory);
        for (int i = 0; i < 10; i++) {
            assertEquals(0, unsafe.getInt(memory + i * INT_MEMORY));
        }
        unsafe.freeMemory(memory);
    }

    @Test
    public void memory() {
        Unsafe unsafe = getUnsafe();
        long memory = unsafe.allocateMemory(INT_MEMORY);
        unsafe.setMemory(memory, INT_MEMORY, (byte) 0);
        unsafe.setMemory(memory + 3, 1, (byte) 1);
        assertEquals(1 << 24, unsafe.getInt(memory));
        unsafe.freeMemory(memory);
    }

    /**
     *
     * @throws Throwable
     */
    @Test
    public void obj() throws Throwable {
        Unsafe unsafe = getUnsafe();
        Object o = unsafe.allocateInstance(Entry.class);
        Assertions.assertTrue(o instanceof Entry);
        Entry entry = (Entry) o;
        Assertions.assertFalse(entry.isUseConstructor());
        assertEquals(0L, entry.getaLong());
        assertEquals(0, entry.getInteger());
        assertEquals(0.0, entry.getaDouble());
        Assertions.assertNull(entry.getString());
    }

    /**
     * x64
     * <pre>
     *    +0 1 2 3 4 5 6 7+
     * 0  |               |
     * 8  |       |<--i-->|
     * 16 |<------l------>|
     * 24 |<------d------>|
     * 32 |<--b-->|<--s-->|
     * </pre>
     */
    @Test
    public void objField() throws Throwable {
        Unsafe unsafe = getUnsafe();
        assertEquals(32, unsafe.objectFieldOffset(Entry.class.getDeclaredField("useConstructor")));
        assertEquals(16, unsafe.objectFieldOffset(Entry.class.getDeclaredField("aLong")));
        assertEquals(12, unsafe.objectFieldOffset(Entry.class.getDeclaredField("integer")));
        assertEquals(24, unsafe.objectFieldOffset(Entry.class.getDeclaredField("aDouble")));
        assertEquals(36, unsafe.objectFieldOffset(Entry.class.getDeclaredField("string")));
    }

    /**
     * 000001
     * 000000
     * 191488
     */
    @Test
    public void objHead() throws Throwable {
        Unsafe unsafe = getUnsafe();
        Entry entry = (Entry) unsafe.allocateInstance(Entry.class);
        System.out.println(Integer.toHexString(unsafe.getInt(entry, 0)));
        System.out.println(Integer.toHexString(unsafe.getInt(entry, 4)));
        System.out.println(Integer.toHexString(unsafe.getInt(entry, 8)));
    }


}
