package com.example.nacos.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {


    private String value;

    public TestController(@Value("${value:hello world}") String value) {
        System.out.println("Create TestController!!");
        this.value = value;
    }

    @GetMapping
    public String test() {
        return value;
    }

}
