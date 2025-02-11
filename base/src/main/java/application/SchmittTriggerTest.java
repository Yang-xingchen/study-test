package application;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SchmittTriggerTest {

    /**
     * C: close  O: open
     *
     * <pre>
     * 90               O
     * 80     O
     * 75 -+-+-+-+-+-+-+-+
     * 50       O
     * 30             C
     * 25 -+-+-+-+-+-+-+-+
     * 20   C
     * 10         C
     *  0 C        C
     * </pre>
     */
    @Test
    public void test() {
        SchmittTrigger schmitt = new SchmittTrigger(25, 75);
        assertFalse(schmitt.open());
        assertFalse(schmitt.update(20));
        assertFalse(schmitt.update(50));
        assertTrue(schmitt.update(80));
        assertTrue(schmitt.update(50));
        assertFalse(schmitt.update(10));
        assertFalse(schmitt.update(0));
        assertFalse(schmitt.update(30));
        assertTrue(schmitt.update(90));
    }

    /**
     * C: close  O: open
     * <pre>
     * 75 -+-+   +-+-+-+-+-+-+-
     * 65    +-+ +
     * 60   C C|O|O O
     * 50      +-+       +-+
     * 40             O O|C|O
     * 35          +-+-+-+ |
     * 25 -+-+-+-+-+       +-+-
     * 0  C
     * </pre>
     */
    @Test
    public void testSet() {
        SchmittTrigger schmitt = new SchmittTrigger(25, 75);
        assertFalse(schmitt.open());
        assertFalse(schmitt.update(60));
        schmitt.setUp(65);
        assertFalse(schmitt.open());
        schmitt.setUp(50);
        assertTrue(schmitt.open());
        schmitt.setUp(75);
        assertTrue(schmitt.open());
        assertTrue(schmitt.update(40));
        schmitt.setDown(35);
        assertTrue(schmitt.open());
        schmitt.setDown(50);
        assertFalse(schmitt.open());
        schmitt.setDown(25);
        assertFalse(schmitt.open());
    }

    /**
     * 施密特触发器
     */
    public static class SchmittTrigger {

        private long up;
        private long down;
        private final AtomicBoolean open = new AtomicBoolean(false);
        private final AtomicLong last = new AtomicLong();

        public boolean open() {
            return open.get();
        }

        public boolean update(long value) {
            boolean oldOpen;
            boolean newOpen;
            do {
                oldOpen = open.get();
                newOpen = value > (open.get() ? down : up);
            } while (!open.compareAndSet(oldOpen, newOpen));
            last.set(value);
            return newOpen;
        }

        /////  /////

        public SchmittTrigger(long down, long up) {
            if (down > up) {
                throw new IllegalArgumentException("down can't greater up");
            }
            this.up = up;
            this.down = down;
        }

        public long getUp() {
            return up;
        }

        public SchmittTrigger setUp(long up) {
            if (up < down) {
                throw new IllegalArgumentException("down can't greater up");
            }
            this.up = up;
            update(last.get());
            return this;
        }

        public long getDown() {
            return down;
        }

        public SchmittTrigger setDown(long down) {
            if (up < down) {
                throw new IllegalArgumentException("down can't greater up");
            }
            this.down = down;
            update(last.get());
            return this;
        }
    }


}
