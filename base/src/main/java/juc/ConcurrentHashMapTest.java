package juc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class ConcurrentHashMapTest {

    static int DEFAULT_CAPACITY = 16;
    static Class<Map.Entry<?, ?>> nodeClass;
    static Field field;

    static {
        try {
            field = ConcurrentHashMap.class.getDeclaredField("table");
            field.setAccessible(true);
            nodeClass = (Class<Map.Entry<?, ?>>) Class.forName("java.util.concurrent.ConcurrentHashMap$Node");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void init() throws NoSuchFieldException, IllegalAccessException {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        Field field = ConcurrentHashMap.class.getDeclaredField("table");
        field.setAccessible(true);

        // lazy load
        Assertions.assertNull(field.get(map));

        // default initialCapacity is DEFAULT_CAPACITY
        map.put(0, 0);
        Assertions.assertEquals(DEFAULT_CAPACITY, ((Map.Entry<Integer, Integer>[])field.get(map)).length);

        map = new ConcurrentHashMap<>(11);

        // lazy load
        Assertions.assertNull(field.get(map));

        // 11 = 0x1011
        map.put(0, 0);
        Assertions.assertEquals(DEFAULT_CAPACITY, ((Map.Entry<Integer, Integer>[])field.get(map)).length);
    }

    @Test
    public void differentHash() throws NoSuchFieldException, IllegalAccessException {
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>();

        map.put(new NoEqualObject(0), 0);
        Class<Map.Entry<?, ?>>[] oneEntryClass = new Class[16];
        oneEntryClass[0] = nodeClass;
        Assertions.assertArrayEquals(
                oneEntryClass,
                Arrays.stream((Map.Entry<NoEqualObject, Integer>[]) field.get(map))
                        .map(entry -> entry == null ? null : entry.getClass())
                        .toArray());
        map.clear();

        // no capacity
        Class<Map.Entry<?, ?>>[] differentHashEntryClass = new Class[16];
        IntStream.range(0, 11).forEach(i -> {
            map.put(new NoEqualObject(i), i);
            differentHashEntryClass[i] = nodeClass;
        });
        Assertions.assertArrayEquals(
                differentHashEntryClass,
                Arrays.stream((Map.Entry<NoEqualObject, Integer>[]) field.get(map))
                        .map(entry -> entry == null ? null : entry.getClass())
                        .toArray());
        Assertions.assertEquals(DEFAULT_CAPACITY, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        // capacity
        map.put(new NoEqualObject(11), 11);
        Assertions.assertEquals((int)(DEFAULT_CAPACITY*0.75), map.size());
        Assertions.assertEquals(DEFAULT_CAPACITY << 1, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        map.clear();
        Assertions.assertEquals(0, map.size());
        Assertions.assertEquals(DEFAULT_CAPACITY << 1, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

    }

    @Test
    public void equalHash() throws IllegalAccessException {
        ConcurrentHashMap<NoEqualObject, Integer> map = new ConcurrentHashMap<>();
        IntStream.range(0, 8).forEach(i -> map.put(new NoEqualObject(0), 0));

        Class<Map.Entry<?, ?>>[] equalHashEntryClass = new Class[16];
        equalHashEntryClass[0] = nodeClass;

        Assertions.assertArrayEquals(
                equalHashEntryClass,
                Arrays.stream((Map.Entry<NoEqualObject, Integer>[]) field.get(map))
                        .map(entry -> entry == null ? null : entry.getClass())
                        .toArray());
        Assertions.assertEquals(8, map.size());
        Assertions.assertEquals(DEFAULT_CAPACITY, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        // ???
        map.put(new NoEqualObject(0), 0);
        Assertions.assertEquals(9, map.size());
        Assertions.assertEquals(128, ((Map.Entry<NoEqualObject, Integer>[])field.get(map)).length);

        equalHashEntryClass = new Class[128];
        equalHashEntryClass[0] = nodeClass;
        Assertions.assertArrayEquals(
                equalHashEntryClass,
                Arrays.stream((Map.Entry<NoEqualObject, Integer>[]) field.get(map))
                        .map(entry -> entry == null ? null : entry.getClass())
                        .toArray());
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
