package com.example.seata.consumer;

public class TestException extends RuntimeException {

    private final Long id;

    public TestException(Throwable cause, Long id) {
        super(cause);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
