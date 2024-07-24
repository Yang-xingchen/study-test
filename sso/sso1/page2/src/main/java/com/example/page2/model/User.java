package com.example.page2.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String name;
    private String pwd;


}
