package com.example.seata.consumer.tcc;

public interface TccTestService {

    Long commit();

    Long rollback();

    void check(String message, Long id, Integer val);

}
