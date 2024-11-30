package time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.MinguoDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;

public class TemporalTest {

    /**
     * <pre>
     *     {@link java.time.temporal.TemporalAccessor}表示日期/时间/时区偏移及其组合，只读
     *     {@link java.time.temporal.Temporal}表示日期/时间/时区偏移及其组合，读写，需要从最小值到最大值是连续且完整的(不需要其他信息补充)
     * </pre>
     */
    @Test
    public void instance() {
        // 年月日
        System.out.println("LocalDate: " + LocalDate.now());
        // 时分秒
        System.out.println("LocalTime: " + LocalTime.now());
        // 年月日时分秒
        System.out.println("LocalDateTime: " + LocalDateTime.now());

        // ZonedDateTime / OffsetDateTime / Instant区别:
        // 1. 三者均表示具体的某一个确切的时刻
        // 2. Instant基于UTC偏移，只封装了epoch开始的秒及毫秒
        // 3. OffsetDateTime在Instant基础上添加时区偏移信息(实现基于LocalDateTime+ZoneOffset)
        // 4. ZonedDateTime在OffsetDateTime基础上添加规则信息(实现基于实现基于LocalDateTime+ZoneId), 可以根据具体区域添加校准规则
        // 年月日时分秒+时区
        System.out.println("ZonedDateTime: " + ZonedDateTime.now());
        System.out.println("OffsetDateTime: " + OffsetDateTime.now());
        // UTC时间
        System.out.println("Instant: " + Instant.now());

        // 时分秒+时区
        System.out.println("OffsetTime: " + OffsetTime.now());

        // 年历+LocalDate / LocalDateTime / ZonedDateTime
        // 如iso、民国历、日本历等
        // ChronoLocalDate / ChronoLocalDateTime / ChronoZonedDateTime
        System.out.println("MinguoDate: " + MinguoDate.now());

        // 时代(非Temporal实例)
        System.out.println("LocalDate: " + LocalDate.now().getEra());
        // 星期(非Temporal实例)
        System.out.println("DayOfWeek: " + DayOfWeek.MONDAY);
        // 月(非Temporal实例)
        System.out.println("Month: " + Month.JANUARY);
        // 年
        System.out.println("Year: " + Year.now());
        // 年月
        System.out.println("YearMonth: " + YearMonth.now());
        // 月日(非Temporal实例)
        System.out.println("MonthDay: " + MonthDay.now());
        // 时区(非Temporal实例)
        System.out.println("ZoneOffset: " + ZoneOffset.of("+8"));
    }

    /**
     * 类型变更
     * 高精度转低精度 / 多个低精度合并
     */
    @Test
    public void convert() {
        ZoneOffset zone = ZoneOffset.of("+8");

        // LocalDateTime <-> LocalDate+LocalTime
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDateTime localDateTime1 = LocalDateTime.of(localDate, localTime);
        Assertions.assertEquals(localDateTime, localDateTime1);

        // ZonedDateTime <-> LocalDateTime+ZoneId
        ZonedDateTime zonedDateTime = localDateTime.atZone(zone);
        LocalDateTime localDateTime2 = zonedDateTime.toLocalDateTime();
        ZoneId zone1 = zonedDateTime.getZone();
        Assertions.assertEquals(localDateTime, localDateTime2);
        Assertions.assertEquals(zone, zone1);

        // Instant <-> ZonedDateTime
        Instant instant = zonedDateTime.toInstant();
        ZonedDateTime zonedDateTime1 = instant.atZone(zone);
        Assertions.assertEquals(zonedDateTime, zonedDateTime1);

        // Instant <-> LocalDateTime
        Instant instant1 = localDateTime.toInstant(zone);
        LocalDateTime localDateTime3 = LocalDateTime.ofInstant(instant1, zone);
        Assertions.assertEquals(localDateTime, localDateTime3);

        // OffsetTime <-> LocalTime+ZoneId
        OffsetTime offsetTime = localTime.atOffset(zone);
        LocalTime localTime1 = offsetTime.toLocalTime();
        ZoneOffset offset = offsetTime.getOffset();
        Assertions.assertEquals(localTime, localTime1);
        Assertions.assertEquals(zone, offset);

        // LocalDateTime -> Year / Month / DayOfWeek / YearMonth / MonthDay
        int year = localDateTime.getYear();
        Year year1 = Year.from(localDateTime);
        Month month = localDateTime.getMonth();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        YearMonth yearMonth = YearMonth.from(localDateTime);
        MonthDay monthDay = MonthDay.from(localDateTime);
        System.out.println("------------------------------------------");
        System.out.println("localDateTime: " + localDateTime);
        System.out.println("year: " + year);
        System.out.println("year1: " + year1);
        System.out.println("month: " + month);
        System.out.println("dayOfWeek: " + dayOfWeek);
        System.out.println("yearMonth: " + yearMonth);
        System.out.println("monthDay: " + monthDay);
        System.out.println("------------------------------------------");

        // ZonedDateTime -> Year / Month / DayOfWeek / YearMonth / MonthDay
        int year2 = zonedDateTime.getYear();
        Year year3 = Year.from(zonedDateTime);
        Month month1 = zonedDateTime.getMonth();
        DayOfWeek dayOfWeek1 = zonedDateTime.getDayOfWeek();
        YearMonth yearMonth1 = YearMonth.from(zonedDateTime);
        MonthDay monthDay1 = MonthDay.from(zonedDateTime);
        System.out.println("------------------------------------------");
        System.out.println("zonedDateTime: " + zonedDateTime);
        System.out.println("year: " + year2);
        System.out.println("year1: " + year3);
        System.out.println("month: " + month1);
        System.out.println("dayOfWeek: " + dayOfWeek1);
        System.out.println("yearMonth: " + yearMonth1);
        System.out.println("monthDay: " + monthDay1);
        System.out.println("------------------------------------------");
    }

    /**
     * 调整
     * 低精度值设置到高精度对应值
     */
    @Test
    public void adjustInto() {
        LocalDate date = LocalDate.of(2024, 11, 30);
        Temporal localDateTime = date.adjustInto(LocalDateTime.of(2024, 11, 29, 12, 0));
        Assertions.assertEquals("2024-11-30T12:00", localDateTime.toString());
        Assertions.assertTrue(localDateTime instanceof LocalDateTime);
    }

    /**
     * 时区转化
     */
    @Test
    public void convertZone() {
        ZonedDateTime src = ZonedDateTime.of(2024, 11, 29, 0, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        // 2024-11-29T00:00+08:00[Asia/Shanghai]
        System.out.println(src);
        // 转换时区，保持时刻不变
        // 2024-11-29T01:00+09:00[Japan]
        System.out.println(src.withZoneSameInstant(ZoneId.of("Japan")));
        // 仅更改时区信息，其他信息不变
        // 2024-11-29T00:00+09:00[Japan]
        System.out.println(src.withZoneSameLocal(ZoneId.of("Japan")));
    }

    /**
     * 日期变更
     */
    @Test
    public void with() {
        // 2024-11-15 周五
        LocalDate date = LocalDate.of(2024, 11, 15);
        // 1. 封装方法: withXxx
        Assertions.assertEquals("2024-11-10", date.withDayOfMonth(10).toString());
        // 2. with+Field
        Assertions.assertEquals("2024-11-10", date.with(ChronoField.DAY_OF_MONTH, 10).toString());
        // 3. adjuster+静态方法
        //// 月初
        Assertions.assertEquals("2024-11-01", date.with(TemporalAdjusters.firstDayOfMonth()).toString());
        //// 月末
        Assertions.assertEquals("2024-11-30", date.with(TemporalAdjusters.lastDayOfMonth()).toString());
        //// 下月初
        Assertions.assertEquals("2024-12-01", date.with(TemporalAdjusters.firstDayOfNextMonth()).toString());
        //// 年初
        Assertions.assertEquals("2024-01-01", date.with(TemporalAdjusters.firstDayOfYear()).toString());
        //// 年底
        Assertions.assertEquals("2024-12-31", date.with(TemporalAdjusters.lastDayOfYear()).toString());
        //// 下年初
        Assertions.assertEquals("2025-01-01", date.with(TemporalAdjusters.firstDayOfNextYear()).toString());
        //// 本月第一个周一
        Assertions.assertEquals("2024-11-04", date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).toString());
        //// 本月最后一个周一
        Assertions.assertEquals("2024-11-25", date.with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY)).toString());
        //// 本月第二个周一
        Assertions.assertEquals("2024-11-11", date.with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.MONDAY)).toString());
        //// 本月最后二个周一
        Assertions.assertEquals("2024-11-18", date.with(TemporalAdjusters.dayOfWeekInMonth(-2, DayOfWeek.MONDAY)).toString());
        //// 下一个周一
        Assertions.assertEquals("2024-11-18", date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toString());
        //// 上一个周一
        Assertions.assertEquals("2024-11-11", date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).toString());
        // 4. adjuster+实例
        //// 指定月份
        Assertions.assertEquals("2024-07-15", date.with(Month.JULY).toString());
        //// 指定星期
        Assertions.assertEquals("2024-11-11", date.with(DayOfWeek.MONDAY).toString());
        //// ... 更多可用实例:
        //// LocalDateTime、LocalDate、LocalTime
        //// OffsetDateTime、OffsetDate、OffsetTime
        //// Instant
        //// DayOfWeek、DayOfMonth、DayOfYear、MonthDay、Month、YearMonth、Year
    }

    /**
     * 计算
     * 包含{@link Temporal#plus}/{@link Temporal#minus}
     */
    @Test
    public void compute() {
        // 2024-11-15 周五
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 15, 12, 0);
        // 1. 封装方法: plusXxx/minusXxx
        Assertions.assertEquals("2024-11-16T12:00", dateTime.plusDays(1).toString());
        // 2. plus/minus+值+单位
        Assertions.assertEquals("2024-11-16T12:00", dateTime.plus(1, ChronoUnit.DAYS).toString());
        // 3. plus/minus+TemporalAmount
        //// Duration: 秒+纳秒
        Assertions.assertEquals("2024-11-16T12:00", dateTime.plus(Duration.ofDays(1)).toString());
        //// Period: 年月日
        Assertions.assertEquals("2024-11-16T12:00", dateTime.plus(Period.of(0, 0, 1)).toString());
    }

    @Test
    public void range() {
        Assertions.assertEquals(29, LocalDate.of(2024, 2, 1).range(ChronoField.DAY_OF_MONTH).getSmallestMaximum());
        Assertions.assertEquals(28, LocalDate.of(2023, 2, 1).range(ChronoField.DAY_OF_MONTH).getSmallestMaximum());
    }

}
