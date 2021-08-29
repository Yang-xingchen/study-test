package other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {

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

}
