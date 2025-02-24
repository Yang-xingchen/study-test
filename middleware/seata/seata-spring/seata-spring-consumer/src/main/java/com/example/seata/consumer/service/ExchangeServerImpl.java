package com.example.seata.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class ExchangeServerImpl implements Server {

    @Autowired
    private ExchangeServerClient exchangeServerClient;

    @Override
    @GetMapping("/create")
    public Long create(int init) {
        return exchangeServerClient.create(init);
    }

    @Override
    @GetMapping("/add")
    public void add(Long id, int i, boolean err) {
        exchangeServerClient.add(id, i, err);
    }

    @Override
    @GetMapping("/get")
    public Integer get(Long id) {
        return exchangeServerClient.get(id);
    }

}
