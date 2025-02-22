package com.example.rule;

import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlowRuleTest {

    @Test
    public void qpsReject() throws Exception {
        String resource = "qps-reject";
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setCount(5);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        FlowRuleManager.loadRules(List.of(rule));

        // 限制5次
        for (int i = 0; i < 5; i++) {
            Assertions.assertTrue(testFor(resource));
        }
        // 超过限制
        for (int i = 0; i < 3; i++) {
            Assertions.assertFalse(testFor(resource));
        }
        // 等待限制过时
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < 5; i++) {
            Assertions.assertTrue(testFor(resource));
        }
        for (int i = 0; i < 3; i++) {
            Assertions.assertFalse(testFor(resource));
        }

    }

    private boolean testFor(String resource) {
        return SphO.entry(resource);
    }

    @Test
    public void qpsPace() {
        String resource = "qps-pace";
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setCount(5);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        FlowRuleManager.loadRules(List.of(rule));

        // 预热
        testFor(resource);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            // 不会失败
            Assertions.assertTrue(testFor(resource));
            // 间隔在200附近, 可能高可能低
            System.out.println(System.currentTimeMillis() - start);
            start = System.currentTimeMillis();
        }
    }

}
