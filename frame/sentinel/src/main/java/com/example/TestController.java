package com.example;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    @SentinelResource("test")
    public String test() {
        return "success 1";
    }

    @GetMapping("/test2")
    @SentinelResource("test2")
    public String test2() {
        return "success 2";
    }

}
