package other;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BigDecimalTest {

    @Test
    public void plus() {
        Assertions.assertNotEquals(0.1 + 0.2, 0.3);
    }

    @Test
    public void plusDecimal() {
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.2");
        BigDecimal er = new BigDecimal("0.3");
        assertEquals(er, a.add(b));
    }

    @Test
    public void scale() {
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.10");

        assertEquals(1, a.scale());
        assertEquals(2, b.scale());
        Assertions.assertNotEquals(a, b);

        assertEquals(a.add(b), b.add(a));
        assertEquals(2, a.add(b).scale());
        assertEquals("0.20", a.add(b).toString());
        assertEquals(a.multiply(b), b.multiply(a));
        assertEquals(3, a.multiply(b).scale());
        assertEquals("0.010", a.multiply(b).toString());
        assertEquals(0, a.divide(b, RoundingMode.HALF_UP).scale());
        assertEquals("1", a.divide(b, RoundingMode.HALF_UP).toString());
        assertEquals(1, b.divide(a, RoundingMode.HALF_UP).scale());
        assertEquals("1.0", b.divide(a, RoundingMode.HALF_UP).toString());

        BigDecimal c = a.setScale(3, RoundingMode.HALF_UP);
        assertEquals("0.100", c.toString());
        assertEquals("0.1", a.round(new MathContext(3, RoundingMode.HALF_UP)).toString());
        assertEquals("0.100", c.round(new MathContext(3, RoundingMode.HALF_UP)).toString());
        assertEquals("0.123", new BigDecimal("0.12345").round(new MathContext(3, RoundingMode.HALF_UP)).toString());
        assertEquals("0.1235", new BigDecimal("0.12345").round(new MathContext(4, RoundingMode.HALF_UP)).toString());

        assertEquals("0", new BigDecimal("1").divide(new BigDecimal("3"), RoundingMode.HALF_UP).toString());
        assertEquals("0.3", new BigDecimal("1.0").divide(new BigDecimal("3"), RoundingMode.HALF_UP).toString());
        assertEquals("0.33", new BigDecimal("1.00").divide(new BigDecimal("3"), RoundingMode.HALF_UP).toString());
    }

    @Test
    public void round() {
        // up 远离0 / down 接近0
        assertEquals("1", new BigDecimal("0.1").setScale(0, RoundingMode.UP).toString());
        assertEquals("-1", new BigDecimal("-0.9").setScale(0, RoundingMode.UP).toString());
        assertEquals("0", new BigDecimal("0.9").setScale(0, RoundingMode.DOWN).toString());
        assertEquals("0", new BigDecimal("-0.1").setScale(0, RoundingMode.DOWN).toString());

        // ceiling 更大 / floor 更小
        assertEquals("1", new BigDecimal("0.1").setScale(0, RoundingMode.CEILING).toString());
        assertEquals("0", new BigDecimal("-0.9").setScale(0, RoundingMode.CEILING).toString());
        assertEquals("0", new BigDecimal("0.9").setScale(0, RoundingMode.FLOOR).toString());
        assertEquals("-1", new BigDecimal("-0.1").setScale(0, RoundingMode.FLOOR).toString());

        // half up 后续为5则进位 / half down 后续为5则舍去
        assertEquals("0", new BigDecimal("0.4").setScale(0, RoundingMode.HALF_UP).toString());
        assertEquals("1", new BigDecimal("0.5").setScale(0, RoundingMode.HALF_UP).toString());
        assertEquals("0", new BigDecimal("-0.4").setScale(0, RoundingMode.HALF_UP).toString());
        assertEquals("-1", new BigDecimal("-0.5").setScale(0, RoundingMode.HALF_UP).toString());
        assertEquals("0", new BigDecimal("0.5").setScale(0, RoundingMode.HALF_DOWN).toString());
        assertEquals("1", new BigDecimal("0.51").setScale(0, RoundingMode.HALF_DOWN).toString());
        assertEquals("1", new BigDecimal("0.6").setScale(0, RoundingMode.HALF_DOWN).toString());
        assertEquals("0", new BigDecimal("-0.5").setScale(0, RoundingMode.HALF_DOWN).toString());
        assertEquals("-1", new BigDecimal("-0.51").setScale(0, RoundingMode.HALF_DOWN).toString());
        assertEquals("-1", new BigDecimal("-0.6").setScale(0, RoundingMode.HALF_DOWN).toString());

        // [IEEE754] half even 若进位为偶数，则half up，否则half down
        assertEquals("0", new BigDecimal("0.5").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("1", new BigDecimal("0.51").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("1", new BigDecimal("0.6").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("1", new BigDecimal("1.4").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("2", new BigDecimal("1.5").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("0", new BigDecimal("-0.5").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("-1", new BigDecimal("-0.6").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("-1", new BigDecimal("-1.4").setScale(0, RoundingMode.HALF_EVEN).toString());
        assertEquals("-2", new BigDecimal("-1.5").setScale(0, RoundingMode.HALF_EVEN).toString());
    }

}
