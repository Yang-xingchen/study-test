package com.example.seata.consumer;

public interface TestService {

    Long success();

    Long fail();

    void check(String message, Long id, Integer val);

}
