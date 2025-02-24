package com.example.seata.consumer.service;

import com.example.seata.consumer.SeataSpringConsumerApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(SeataSpringConsumerApplication.PROVIDER_NAME)
public interface FeignServerClient {

    @GetMapping("/create")
    Long create(@RequestParam("init") Integer init);

    @GetMapping("/add")
    void add(@RequestParam("id") Long id, @RequestParam("i") Integer i, @RequestParam("err") Boolean err);

    @GetMapping("/get")
    Integer get(@RequestParam("id") Long id);

}
