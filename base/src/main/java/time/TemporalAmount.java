package time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TemporalAmount {

    @Test
    public void parse() {
        // +-表示符号
        // P关键字
        // D天
        // T关键字，如果无时间可省略
        // H小时
        // M分
        // S.秒
        // ([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?
        Assertions.assertEquals(TimeUnit.DAYS.toSeconds(1), Duration.parse("P1D").getSeconds());
        Assertions.assertEquals(TimeUnit.DAYS.toSeconds(1), Duration.parse("P+1D").getSeconds());
        Assertions.assertEquals(-TimeUnit.DAYS.toSeconds(1), Duration.parse("P-1D").getSeconds());
        Assertions.assertEquals(TimeUnit.HOURS.toSeconds(1), Duration.parse("PT1H").getSeconds());
        Assertions.assertEquals(TimeUnit.MILLISECONDS.toNanos(123), Duration.parse("PT0.123S").getNano());
        Assertions.assertEquals(-TimeUnit.DAYS.toSeconds(1), Duration.parse("-P1D").getSeconds());
        Assertions.assertEquals(TimeUnit.DAYS.toSeconds(1), Duration.parse("-P-1D").getSeconds());

        Assertions.assertEquals(TimeUnit.DAYS.toSeconds(1) + TimeUnit.HOURS.toSeconds(1),
                Duration.parse("P1DT1H").getSeconds());
        Assertions.assertEquals(TimeUnit.DAYS.toSeconds(1) - TimeUnit.HOURS.toSeconds(1),
                Duration.parse("P1DT-1H").getSeconds());
    }

}
