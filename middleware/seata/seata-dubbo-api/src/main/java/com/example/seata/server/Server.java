package com.example.seata.server;

public interface Server {

    Long create(int init);

    void add(Long id, int i, boolean err);

    Integer get(Long id);

}
