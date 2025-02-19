package com.example.nacos.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(NacosConsumerApplication.PROVIDER_NAME)
public interface ServerClientByFeign {

    @GetMapping
    String test();

}
