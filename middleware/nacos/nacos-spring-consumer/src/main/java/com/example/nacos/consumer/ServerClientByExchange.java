package com.example.nacos.consumer;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://nacosServer/")
public interface ServerClientByExchange {

    @GetExchange
    String test();

}
