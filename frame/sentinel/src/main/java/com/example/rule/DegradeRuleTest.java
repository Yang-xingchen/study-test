package com.example.rule;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DegradeRuleTest {

    @Test
    public void slow() throws InterruptedException {
        String resource = "authorityBlack";
        DegradeRule rule = new DegradeRule();
        rule.setResource(resource);
        // 响应时间(ms)
        rule.setCount(100);
        // 断开时间，超时后转为半开
        rule.setTimeWindow(3);
        // 慢调用比例，当statIntervalMs时间内慢调用比例大于该值时熔断
        rule.setSlowRatioThreshold(0.4);
        // 统计周期
        rule.setStatIntervalMs(1000);
        // 最小请求数，低于该值时不会熔断
        rule.setMinRequestAmount(2);
        rule.setLimitApp("default");
        rule.setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType());
        DegradeRuleManager.loadRules(List.of(rule));

        // 低于时间，不熔断
        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(testFor(resource, 50), "pass: " + i);
        }
        TimeUnit.SECONDS.sleep(1);

        Assertions.assertTrue(testFor(resource, 50), "fast");
        Assertions.assertTrue(testFor(resource, 150), "first slow");
        // 高于时间，且比例0.5>0.4，熔断
        Assertions.assertFalse(testFor(resource, 50), "fast");
        for (int i = 0; i < 10; i++) {
            Assertions.assertFalse(testFor(resource, 50), "slow: " + i);
        }
        TimeUnit.SECONDS.sleep(3);

        // 半开状态
        Assertions.assertTrue(testFor(resource, 150), "half open slow");
        // 慢请求重新熔断
        Assertions.assertFalse(testFor(resource, 50), "close");
        TimeUnit.SECONDS.sleep(3);

        // 半开状态
        Assertions.assertTrue(testFor(resource, 50), "half open fast");
        // 结束熔断
        Assertions.assertTrue(testFor(resource, 50), "open");

        // 低于最小请求数，不会熔断
        for (int i = 0; i < 5; i++) {
            TimeUnit.SECONDS.sleep(1);
            Assertions.assertTrue(testFor(resource, 150), "min request: " + i);
        }

    }

    public boolean testFor(String resource, int delay) throws InterruptedException {
        try (Entry entry = SphU.entry(resource)) {
            TimeUnit.MILLISECONDS.sleep(delay);
            return true;
        } catch (BlockException e) {
            return false;
        }
    }

}
