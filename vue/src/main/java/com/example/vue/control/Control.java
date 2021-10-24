package com.example.vue.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Control {

    @GetMapping("/datalist")
    public List<String> dataList() {
        return List.of("data1", "data2", "data3");
    }
}
