package com.example.userCenter.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginInfo {

    private boolean login;
    private List<String> appList;
    private String backUrl;
    private String uuid;

}
