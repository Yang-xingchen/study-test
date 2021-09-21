package other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Over {

    private static final String Object = "object";
    private static final String FATHER = "father";
    private static final String SON = "son";

    public static class Father {
        public String get() {
            return FATHER;
        }
    }

    public static class Son extends Father {
        @Override
        public String get() {
            return SON;
        }
    }

    public static String get(Object object) {
        return Object;
    }

    public static String get(Father father) {
        return FATHER;
    }

    public static String get(Son son) {
        return SON;
    }

    @Test
    public void over() {
        Father son = new Son();
        assertEquals(SON, son.get());
        assertEquals(FATHER, get(son));
        assertEquals(SON, get((Son)son));
    }

}
