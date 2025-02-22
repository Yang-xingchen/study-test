package com.example.rule;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AuthorityRuleTest {

    @Test
    public void white() {
        String resource = "authorityWhite";
        AuthorityRule rule = new AuthorityRule();
        rule.setResource(resource);
        rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
        rule.setLimitApp("origin1,origin2");
        AuthorityRuleManager.loadRules(List.of(rule));

        Assertions.assertTrue(testFor(resource, "origin1"));
        Assertions.assertTrue(testFor(resource, "origin2"));
        Assertions.assertFalse(testFor(resource, "origin3"));
        Assertions.assertFalse(testFor(resource, "origin4"));
    }

    @Test
    public void black() {
        String resource = "authorityBlack";
        AuthorityRule rule = new AuthorityRule();
        rule.setResource(resource);
        rule.setStrategy(RuleConstant.AUTHORITY_BLACK);
        rule.setLimitApp("origin3,origin4");
        AuthorityRuleManager.loadRules(List.of(rule));

        Assertions.assertTrue(testFor(resource, "origin1"));
        Assertions.assertTrue(testFor(resource, "origin2"));
        Assertions.assertFalse(testFor(resource, "origin3"));
        Assertions.assertFalse(testFor(resource, "origin4"));
    }

    private boolean testFor(String resource, String origin) {
        ContextUtil.enter(resource, origin);
        try (Entry entry = SphU.entry(resource)) {
            return true;
        } catch (BlockException ex) {
            return false;
        } finally {
            ContextUtil.exit();
        }
    }

}
