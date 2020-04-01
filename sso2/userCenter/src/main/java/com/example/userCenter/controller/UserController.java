package com.example.userCenter.controller;

import com.example.userCenter.model.LoginInfo;
import com.example.userCenter.model.Response;
import com.example.userCenter.model.User;
import com.example.userCenter.server.UserServer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@Controller
@ConfigurationProperties("user-center")
public class UserController {

    @Autowired
    private UserServer userServer;

    private List<String> appList = new ArrayList<>();

    private Map<String, String> appMap = new HashMap<>();

    private Map<String, User> loginList = new ConcurrentHashMap<>();

    @GetMapping({"/login", "/login.html"})
    public String loginPage(@RequestParam("back") String back) {
        return "/login.html";
    }

    @PostMapping("/login")
    @ResponseBody
    public Response<LoginInfo> login(@RequestParam("back") String back,
                                     @RequestParam("name") String name,
                                     @RequestParam("pwd") String pwd) {
        User user = User.builder().name(name).pwd(pwd).build();
        log.info("login: " + user.toString() + " back:" + back);
        String uuid = UUID.randomUUID().toString();
        boolean login = userServer.login(user);
        if (login) {
            loginList.put(uuid, user);
        }
        return Response
                .success(LoginInfo
                        .builder()
                        .login(login)
                        .backUrl(appMap.get(back))
                        .uuid(uuid)
                        .appList(appList)
                        .build());
    }


    @PostMapping("/checkCookie")
    @ResponseBody
    public Response<User> checkCookie(@RequestBody String cookie) {
        if (loginList.containsKey(cookie)) {
            log.info("checkCookie true:" + cookie);
            return Response.success(loginList.get(cookie));
        }
        log.info("checkCookie false:" + cookie);
        return Response.fail(null);
    }
}
