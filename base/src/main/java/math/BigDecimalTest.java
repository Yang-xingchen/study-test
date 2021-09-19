package math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        Assertions.assertEquals(er, a.add(b));
    }

    @Test
    public void scale() {
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.10");

        Assertions.assertEquals(1, a.scale());
        Assertions.assertEquals(2, b.scale());
        Assertions.assertNotEquals(a, b);

        Assertions.assertEquals(a.add(b), b.add(a));
        Assertions.assertEquals(2, a.add(b).scale());
        Assertions.assertEquals(a.multiply(b), b.multiply(a));
        Assertions.assertEquals(3, a.multiply(b).scale());
    }

}
