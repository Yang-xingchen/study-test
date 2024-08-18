package com.example.nacos.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class TestController {

    @Autowired
    private ServerClientByFeign serverClientByFeign;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestClient restClient;
    @Autowired
    private ServerClientByExchange serverClientByExchange;

    @GetMapping("/byFeign")
    public String byFeign() {
        return "consumer[feign]: " + serverClientByFeign.test();
    }

    @GetMapping("/byRestTemplate")
    public String byRestTemplate() {
        return "consumer[restTemplate]: " + restTemplate.getForObject("http://nacosServer/", String.class);
    }

    @GetMapping("/byRestClient")
    public String byRestClient() {
        return "consumer[restClient]: " + restClient.get().uri("http://nacosServer/").retrieve().body(String.class);
    }

    @GetMapping("/byExchange")
    public String byExchange() {
        return "consumer[httpExchange]: " + serverClientByExchange.test();
    }

}
