package com.example.page2.controller;

import com.example.page2.model.Response;
import com.example.page2.model.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Data
@Slf4j
public class RootController {

    public static final String CHECK_COOKIE_URL = "http://localhost:8080/checkCookie";
    public static final String LOGIN_URL = "http://localhost:8080/login";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(@CookieValue(value = "uuid", required = false) String uuid) {
        if (uuid == null) {
            return "/index.html";
        }
        Response r = restTemplate.postForObject(CHECK_COOKIE_URL, uuid, Response.class);
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
        Response r = restTemplate.postForObject(LOGIN_URL, user, Response.class);
        if (r != null && Response.SUCCESS.equals(r.getStatus())) {
            log.info(r.toString());
            response.addCookie(new Cookie("uuid", r.getData().toString()));
            return "/home.html";
        }
        return "/index.html";
    }

    @GetMapping("/getCookie")
    @ResponseBody
    public Response<String> getCookie(@CookieValue("uuid") String uuid) {
        log.info("getCookie:" + uuid);
        return Response.success(uuid);
    }

    @GetMapping("/setCookie")
    @ResponseBody
    public Response<String> setCookie(@CookieValue(value = "uuid", required = false) String uuid,
                                      String cookie,
                                      HttpServletResponse response) {
        log.info("setCookie uuid:" + uuid);
        if (uuid == null || !uuid.equals(cookie)) {
            log.info("setCookie:" + cookie);
            response.addCookie(new Cookie("uuid", cookie));
        }
        return Response.success("");
    }

}
