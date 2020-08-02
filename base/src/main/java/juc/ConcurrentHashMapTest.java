package juc;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class ConcurrentHashMapTest {

    /**
     * 16
     */
    static int DEFAULT_CAPACITY;
    /**
     * 0.75f
     */
    static float LOAD_FACTOR;
    /**
     * 8
     */
    static int TREEIFY_THRESHOLD;

    static Class<Map.Entry<?, ?>> nodeClass;
    static Class<Map.Entry<?, ?>> treeClass;

    static Field field;

    static <R> R getValueByField(String name) throws IllegalAccessException, NoSuchFieldException {
        Field f = ConcurrentHashMap.class.getDeclaredField(name);
        f.setAccessible(true);
        return (R) f.get(null);
    }

    static {
        try {
            DEFAULT_CAPACITY = getValueByField("DEFAULT_CAPACITY");
            LOAD_FACTOR = getValueByField("LOAD_FACTOR");
            TREEIFY_THRESHOLD = getValueByField("TREEIFY_THRESHOLD");

            field = ConcurrentHashMap.class.getDeclaredField("table");
            field.setAccessible(true);

            nodeClass = (Class<Map.Entry<?, ?>>) Class.forName("java.util.concurrent.ConcurrentHashMap$Node");
            treeClass = (Class<Map.Entry<?, ?>>) Class.forName("java.util.concurrent.ConcurrentHashMap$TreeBin");
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void init() throws NoSuchFieldException, IllegalAccessException {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        Field field = ConcurrentHashMap.class.getDeclaredField("table");
        field.setAccessible(true);

        // lazy load, map is null
        assertNull(field.get(map));

        // default initialCapacity is DEFAULT_CAPACITY(16)
        map.put(0, 0);
        assertEquals(DEFAULT_CAPACITY, ((Map.Entry<Integer, Integer>[])field.get(map)).length);

        map = new ConcurrentHashMap<>(11);

        // lazy load
        assertNull(field.get(map));

        // 1 + 11 / 0.75 = 15, 15 = 0b1111 => 16 = 0b10000
        // @See{java.util.concurrent.ConcurrentHashMap.ConcurrentHashMap(int, float, int)}
        // why +1?
        map.put(0, 0);
        assertEquals(0b10000, ((Map.Entry<Integer, Integer>[])field.get(map)).length);

        map = new ConcurrentHashMap<>(12);

        // 1 + 12 / 0.75 = 17, 17 = 0b10001 => 32 = 0b100000
        map.put(0, 0);
        assertEquals(0b100000, ((Map.Entry<Integer, Integer>[])field.get(map)).length);
    }

    static Object[] getClassArray(ConcurrentHashMap<NoEqualObject, Integer> map)
            throws IllegalAccessException {
        return Arrays.stream((Map.Entry<NoEqualObject, Integer>[]) field.get(map))
                .map(entry -> entry == null ? null : entry.getClass())
                .toArray();
    }

    @Test
    public void differentHash() throws IllegalAccessException {
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>();

        map.put(new NoEqualObject(0), 0);
        Class<Map.Entry<?, ?>>[] oneEntryClass = new Class[DEFAULT_CAPACITY];
        oneEntryClass[0] = nodeClass;
        assertArrayEquals(oneEntryClass, getClassArray(map));
        map.clear();

        // no capacity
        Class<Map.Entry<?, ?>>[] differentHashEntryClass = new Class[DEFAULT_CAPACITY];
        IntStream.range(0, 11).forEach(i -> {
            map.put(new NoEqualObject(i), i);
            differentHashEntryClass[i] = nodeClass;
        });
        assertArrayEquals(differentHashEntryClass, getClassArray(map));
        assertEquals(DEFAULT_CAPACITY, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        // capacity, sumCount() >= sizeCtl
        // @See{java.util.concurrent.ConcurrentHashMap.addCount}
        map.put(new NoEqualObject(11), 11);
        assertEquals((int)(DEFAULT_CAPACITY*LOAD_FACTOR), map.size());
        assertEquals(DEFAULT_CAPACITY << 1, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        map.clear();
        assertEquals(0, map.size());
        assertEquals(DEFAULT_CAPACITY << 1, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);
    }

    @Test
    public void equalHash() throws IllegalAccessException {
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>();
        IntStream.range(0, 8).forEach(i -> map.put(new NoEqualObject(0), 0));

        Class<Map.Entry<?, ?>>[] equalHashEntryClass = new Class[DEFAULT_CAPACITY];
        equalHashEntryClass[0] = nodeClass;

        assertArrayEquals(equalHashEntryClass, getClassArray(map));
        assertEquals(8, map.size());
        assertEquals(DEFAULT_CAPACITY, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        // ???
        map.put(new NoEqualObject(0), 0);
        assertEquals(9, map.size());
        assertEquals(128, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        equalHashEntryClass = new Class[128];
        equalHashEntryClass[0] = nodeClass;
        assertArrayEquals(equalHashEntryClass, getClassArray(map));
    }

    @Test
    public void treeify() throws IllegalAccessException {
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>();
        IntStream.range(0, 9).forEach(i -> map.put(new NoEqualObject(0), 0));

        // not treeify
        Class<Map.Entry<?, ?>>[] noTreeClasses = new Class[128];
        noTreeClasses[0] = nodeClass;
        assertEquals(9, map.size());
        assertArrayEquals(noTreeClasses,getClassArray(map));

        // treeify
        // equal_hash_entry=10 > TREEIFY_THRESHOLD=8 && tab.length=128 >= MIN_TREEIFY_CAPACITY = 64
        // @See{java.util.concurrent.ConcurrentHashMap.treeifyBin}
        map.put(new NoEqualObject(0), 0);
        Class<Map.Entry<?, ?>>[] treeClasses = new Class[128];
        treeClasses[0] = treeClass;
        assertEquals(10, map.size());
        assertArrayEquals(treeClasses, getClassArray(map));
    }

    @Test
    public void treeify2() throws IllegalAccessException {
        // 1 + 47 / 0.75 = 63 => 64
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>(47);
        IntStream.range(0, TREEIFY_THRESHOLD).forEach(i -> map.put(new NoEqualObject(0), 0));
        Class<Map.Entry<?, ?>>[] classes = new Class[64];
        classes[0] = nodeClass;
        assertEquals(TREEIFY_THRESHOLD, map.size());
        assertEquals(64, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);
        assertArrayEquals(classes, getClassArray(map));

        // treeify
        // equal_hash_entry=9 > TREEIFY_THRESHOLD=8 && tab.length=64 >= MIN_TREEIFY_CAPACITY = 64
        // @See{java.util.concurrent.ConcurrentHashMap.treeifyBin}
        map.put(new NoEqualObject(0), 0);
        classes[0] = treeClass;
        assertEquals(TREEIFY_THRESHOLD + 1, map.size());
        assertEquals(64, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);
        assertArrayEquals(classes, getClassArray(map));
    }

    static class NoEqualObject {
        final int hash;

        NoEqualObject(int hash) {
            this.hash = hash;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

}
