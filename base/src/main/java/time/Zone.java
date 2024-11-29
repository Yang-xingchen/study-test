package time;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

/**
 * <pre>
 * 时区{@link ZoneId}有两个实例:
 * {@link java.time.ZoneOffset}: 基于UTC偏移
 * {@link java.time.ZoneRegion}: 基于地区编码
 * </pre>
 */
public class Zone {

    @Test
    public void zoneId() {
        System.out.println(ZoneId.of("Z"));
        System.out.println(ZoneId.of("+8"));
        System.out.println(ZoneId.of("+08"));
        System.out.println(ZoneId.of("+08:00"));
        System.out.println(ZoneId.of("+0800"));
        System.out.println(ZoneId.of("GMT"));
        System.out.println(ZoneId.of("UTC"));
        System.out.println(ZoneId.of("UTC+8"));
        System.out.println(ZoneId.of("UTC+08:00"));
        System.out.println(ZoneId.of("UTC+0800"));
        System.out.println(ZoneId.of("UT"));
        System.out.println(ZoneId.of("UTC+8"));
        System.out.println(ZoneId.of("Asia/Shanghai"));
    }

    @Test
    public void zoneIds() {
        ZoneId.getAvailableZoneIds().stream().sorted().forEach(zone -> System.out.println(zone + ": " + ZoneId.of(zone).getRules()));
    }

}
