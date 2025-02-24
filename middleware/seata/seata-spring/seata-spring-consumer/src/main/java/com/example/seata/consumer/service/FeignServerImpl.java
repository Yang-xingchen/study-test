package com.example.seata.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class FeignServerImpl implements Server {

    @Autowired
    private FeignServerClient feignServerClient;

    @Override
    @GetMapping("/create")
    public Long create(int init) {
        return feignServerClient.create(init);
    }

    @Override
    @GetMapping("/add")
    public void add(Long id, int i, boolean err) {
        feignServerClient.add(id, i, err);
    }

    @Override
    @GetMapping("/get")
    public Integer get(Long id) {
        return feignServerClient.get(id);
    }

}
