package com.example.seata.consumer.service;

public interface Server {

    Long create(int init);

    void add(Long id, int i, boolean err);

    Integer get(Long id);

}
