package com.example.page1.controller;

import com.example.page1.model.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@Data
public class RootController {

    @Value("${user-center.check-url}")
    public String CHECK_COOKIE_URL = "http://localhost:8080/checkCookie";
    @Value("${user-center.login-url}")
    public String LOGIN_URL;
    @Value("${user-center.app-id}")
    public String APP_ID;

    @Autowired
    public RestTemplate restTemplate;

    @GetMapping("/")
    public String index(
            @CookieValue(value = "uuid", required = false) String uuid,
            RedirectAttributes redirectAttributes) {
        if (uuid == null) {
            log.info("index: uuid not find");
            redirectAttributes.addAttribute("back", APP_ID);
            return "redirect:" + LOGIN_URL;
        }
        Response<?> response = restTemplate.postForObject(CHECK_COOKIE_URL, uuid, Response.class);
        if (response == null || !Response.SUCCESS.equals(response.getStatus())) {
            log.info("index: uuid err: " +uuid);
            redirectAttributes.addAttribute("back", APP_ID);
            return "redirect:" + LOGIN_URL;
        }
        log.info("index: success");
        return "index.html";
    }

    @GetMapping("/setCookie")
    @ResponseBody
    public Response<String> setCookie(String cookie,
                                      HttpServletResponse response) {
        log.info("setCookie:" + cookie);
        response.addCookie(new Cookie("uuid", cookie));
        return Response.success("");
    }

}
