package com.example.seata.consumer.at;

public interface AtTestService {

    Long commit();

    Long rollback();

    void check(String message, Long id, Integer val);

}
