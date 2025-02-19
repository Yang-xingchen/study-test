package com.example.nacos.consumer;

import com.example.nacos.server.Server;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @DubboReference(group = "dubbo")
    private Server server;

    @GetMapping("/byDubbo")
    public String byFeign() {
        return "consumer[dubbo]: " + server.test();
    }

}

