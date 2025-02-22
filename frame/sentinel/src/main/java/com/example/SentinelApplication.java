package com.example;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRule() {
        return args -> {
            List<FlowRule> rules = Stream.of("test", "test2").map(resource -> {
                FlowRule rule = new FlowRule();
                rule.setResource(resource);
                rule.setCount(1);
                rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
                rule.setLimitApp("default");
                return rule;
            }).toList();
            FlowRuleManager.loadRules(rules);
        };
    }

    /**
     * 异常处理器，sentinel会抛出{@link BlockException}异常, 需要手动处理
     */
    @ControllerAdvice
    public static class BlockExceptionHandel extends ResponseEntityExceptionHandler {

        @ExceptionHandler(BlockException.class)
        public final ResponseEntity<Object> handleBlockException(Exception ex, WebRequest request) throws Exception {
            return createResponseEntity("Blocked by Sentinel (flow limiting)", null, HttpStatusCode.valueOf(429), request);
        }

    }

}
