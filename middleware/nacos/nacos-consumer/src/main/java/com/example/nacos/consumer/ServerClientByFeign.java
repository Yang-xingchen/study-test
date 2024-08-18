package com.example.nacos.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("nacosServer")
public interface ServerClientByFeign {

    @GetMapping
    String test();

}
