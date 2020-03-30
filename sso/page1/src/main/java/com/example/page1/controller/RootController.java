package com.example.page1.controller;

import com.example.page1.model.Response;
import com.example.page1.model.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Data
@Slf4j
public class RootController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(@CookieValue(value = "uuid", required = false) String uuid) {
        if (uuid == null) {
            return "/index.html";
        }
        Response r = restTemplate.postForObject("http://localhost:8080/checkCookie",
                uuid,
                Response.class);
        if (r == null || !Response.SUCCESS.equals(r.getStatus())) {
            return "/index.html";
        }
        log.info(r.toString());
        return "home.html";
    }

    @PostMapping("/login")
    public String login(String name, String pwd, HttpServletResponse response) {
        User user = User.builder().name(name).pwd(pwd).build();
        log.info(user.toString());
        Response r = restTemplate.postForObject("http://localhost:8080/login",
                user,
                Response.class);
        if (r != null && Response.SUCCESS.equals(r.getStatus())) {
            log.info(r.toString());
            response.addCookie(new Cookie("uuid", r.getData().toString()));
            return "/home.html";
        }
        return "/index.html";
    }

    @GetMapping("/getCookie")
    public Response<String> getCookie(@CookieValue("uuid") String uuid) {
        return Response.success(uuid);
    }

    @PostMapping("/setCookie")
    public Response<String> setCookie(String cookie, HttpServletResponse response) {
        response.addCookie(new Cookie("uuid", cookie));
        return Response.success("");
    }

}
