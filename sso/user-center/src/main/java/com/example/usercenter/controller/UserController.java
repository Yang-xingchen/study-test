package com.example.usercenter.controller;

import com.example.usercenter.model.Response;
import com.example.usercenter.model.User;
import com.example.usercenter.server.UserServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
public class UserController {

    private ConcurrentHashMap<String, User> loginList = new ConcurrentHashMap<>();
    @Autowired
    private UserServer userServer;

    public static final List<String> appList = List.of(
            "http://localhost:8081/setCookie",
            "http://localhost:8082/setCookie"
    );

    @PostMapping(value = "/login")
    public Response<String> login(@RequestBody User user) {
        log.info(user.toString());
        if (userServer.login(user)) {
            String uuid = UUID.randomUUID().toString();
            loginList.put(uuid, user);
            log.info("login");
            return Response.success(uuid);
        }
        return Response.fail("fail");
    }

    @PostMapping("/checkCookie")
    public Response<User> checkCookie(@RequestBody String cookie) {
        log.info("checkCookie:" + cookie);
        if (loginList.containsKey(cookie)) {
            log.info("checkCookie: true");
            return Response.success(loginList.get(cookie));
        }
        return Response.fail(null);
    }

    @GetMapping("/appList")
    public Response<List<String>> appList(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        return Response.success(appList);
    }

}
