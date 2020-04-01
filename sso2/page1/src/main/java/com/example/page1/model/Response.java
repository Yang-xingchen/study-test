package com.example.page1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    private String status;

    private T data;

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS, data);
    }

    public static <T> Response<T> fail(T data) {
        return new Response<>(FAIL, data);
    }
}
