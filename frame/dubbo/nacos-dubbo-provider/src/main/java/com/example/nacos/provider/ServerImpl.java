package com.example.nacos.provider;

import com.example.nacos.server.Server;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@DubboService(group = "dubbo", protocol = {"dubbo", "rest"})
@RefreshScope
@RestController
public class ServerImpl implements Server {

    private String value;

    public ServerImpl(@Value("${value:hello world}") String value) {
        System.out.println("Create ServerImpl!!");
        this.value = value;
    }

    @Override
    public String test() {
        return value;
    }

}
