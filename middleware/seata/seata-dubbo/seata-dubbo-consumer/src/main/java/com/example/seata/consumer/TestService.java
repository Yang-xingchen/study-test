package com.example.seata.consumer;

public interface TestService {

    Long commit();

    Long rollback();

    void check(String message, Long id, Integer val);

}
