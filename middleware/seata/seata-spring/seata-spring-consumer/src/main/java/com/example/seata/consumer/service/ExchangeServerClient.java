package com.example.seata.consumer.service;

import com.example.seata.consumer.SeataSpringConsumerApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://" + SeataSpringConsumerApplication.PROVIDER_NAME)
public interface ExchangeServerClient {

    @GetExchange("/create")
    Long create(@RequestParam("init") Integer init);

    @GetExchange("/add")
    void add(@RequestParam("id") Long id, @RequestParam("i") Integer i, @RequestParam("err") Boolean err);

    @GetExchange("/get")
    Integer get(@RequestParam("id") Long id);

}
