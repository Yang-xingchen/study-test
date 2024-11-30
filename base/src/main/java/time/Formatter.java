package time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class Formatter {

    @Test
    public void parse() {
        ZonedDateTime dateTime = ZonedDateTime.of(2024, 7, 30, 13, 0, 0, 0, ZoneId.systemDefault());
        // 预设
        Assertions.assertEquals("2024-07-30T13:00:00+08:00[Asia/Shanghai]", dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
        // ''内表示特定字符
        Assertions.assertEquals("2024-07-30T13:00:00", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        // ''表示'
        Assertions.assertEquals("2024'07'30 13:00:00", dateTime.format(DateTimeFormatter.ofPattern("yyyy''MM''dd HH:mm:ss")));
        // []表示可选
        Assertions.assertEquals("2024-07-30 13:00:00", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm:ss]")));
        Assertions.assertEquals("2024-07-30", LocalDate.of(2024, 7, 30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm:ss]")));
        // yyyy yy
        Assertions.assertEquals("2024 24", dateTime.format(DateTimeFormatter.ofPattern("yyyy yy")));

//        //// 系统区域: 中国
//        // MMMMM MMMM MM M
//        Assertions.assertEquals("7 七月 7月 07 7", dateTime.format(DateTimeFormatter.ofPattern("MMMMM MMMM LLL MM M")));
//        // LLLLL LLLL LL L
//        Assertions.assertEquals("7 七月 7月 07 7", dateTime.format(DateTimeFormatter.ofPattern("LLLLL LLLL LLL LL L")));
//        // QQQQQ QQQQ QQQ QQ Q
//        Assertions.assertEquals("3 第三季度 3季度 03 3", dateTime.format(DateTimeFormatter.ofPattern("QQQQQ QQQQ QQQ QQ Q")));
//        // EEEEE EEEE E
//        Assertions.assertEquals("二 星期二 周二", dateTime.format(DateTimeFormatter.ofPattern("EEEEE EEEE E")));
//        // eeeee eeee e
//        Assertions.assertEquals("二 星期二 2", dateTime.format(DateTimeFormatter.ofPattern("eeeee eeee e")));
//        // a
//        Assertions.assertEquals("下午", dateTime.format(DateTimeFormatter.ofPattern("a")));
//        // VV vvvv v
//        Assertions.assertEquals("Asia/Shanghai 中国时间 CT", dateTime.format(DateTimeFormatter.ofPattern("VV vvvv v")));
//        //// 系统区域: 美国
//        // MMMMM MMMM MM M
//        Assertions.assertEquals("J July Jul 07 7", dateTime.format(DateTimeFormatter.ofPattern("MMMMM MMMM LLL MM M")));
//        // LLLLL LLLL LL L
//        Assertions.assertEquals("J July Jul 07 7", dateTime.format(DateTimeFormatter.ofPattern("LLLLL LLLL LLL LL L")));
//        // QQQQQ QQQQ QQQ QQ Q
//        Assertions.assertEquals("3 3rd quarter Q3 03 3", dateTime.format(DateTimeFormatter.ofPattern("QQQQQ QQQQ QQQ QQ Q")));
//        // EEEEE EEEE E
//        Assertions.assertEquals("T Tuesday Tue", dateTime.format(DateTimeFormatter.ofPattern("EEEEE EEEE E")));
//        // eeeee eeee e
//        Assertions.assertEquals("T Tuesday 3", dateTime.format(DateTimeFormatter.ofPattern("eeeee eeee e")));
//        // a
//        Assertions.assertEquals("PM", dateTime.format(DateTimeFormatter.ofPattern("a")));
//        // VV vvvv v
//        Assertions.assertEquals("Asia/Shanghai China Time CT", dateTime.format(DateTimeFormatter.ofPattern("VV vvvv v")));

        // H h
        Assertions.assertEquals("13 1", dateTime.format(DateTimeFormatter.ofPattern("H h")));

        // Z ZZZZ ZZZZZ z
        Assertions.assertEquals("+0800 GMT+08:00 +08:00 CST", dateTime.format(DateTimeFormatter.ofPattern("Z ZZZZ ZZZZZ z")));
        // X x
        Assertions.assertEquals("+08 +08", dateTime.format(DateTimeFormatter.ofPattern("X x")));
        // O OOOO
        Assertions.assertEquals("GMT+8 GMT+08:00", dateTime.format(DateTimeFormatter.ofPattern("O OOOO")));
    }

    @Test
    public void build() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 7, 30, 13, 0);
        Assertions.assertEquals("2024:07 星期二+-+13:00:00", dateTime.format(new DateTimeFormatterBuilder()
                // 添加一个数值
                .appendValue(ChronoField.YEAR)
                // 添加一个字符
                .appendLiteral(':')
                // 添加数值可设置长度，不可低于原长度
                .appendValue(ChronoField.MONTH_OF_YEAR, 2)
                .appendLiteral(' ')
                // 添加一个文本值
                .appendText(ChronoField.DAY_OF_WEEK)
                // 添加一个字符串
                .appendLiteral("+-+")
                // 添加其他DateTimeFormatter
                .append(ISO_LOCAL_TIME)
                // 构建
                .toFormatter()));
    }

}
