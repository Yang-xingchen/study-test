package com.example.nacos.consumer;

import com.example.nacos.server.Server;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @DubboReference(group = "dubbo", protocol = "dubbo")
    private Server server;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/byDubbo")
    public String byDubbo() {
        return "consumer[dubbo]: " + server.test();
    }

    @GetMapping("/byRest")
    public String byRest() {
        return "consumer[rest]: " + restTemplate.getForObject("http://nacosDubboProvider/", String.class);
    }

}

