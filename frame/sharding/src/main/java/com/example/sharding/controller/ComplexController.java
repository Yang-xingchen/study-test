package com.example.sharding.controller;

import com.example.sharding.mapper.ComplexMapper;
import com.example.sharding.model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RequestMapping("/complex")
@RestController
public class ComplexController {

    @Autowired
    private ComplexMapper complexMapper;

    @GetMapping("/generate")
    public String generate() {
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            TestModel testModel = new TestModel();
            testModel.setId((long) i);
            testModel.setUid("user" + Math.abs(random.nextInt() % 100));
            testModel.setGid("good" + Math.abs(random.nextInt() % 100));
            complexMapper.insert(testModel);
        }
        return "SUCCESS";
    }

    @GetMapping("/getByUser")
    public List<TestModel> getByUser(@RequestParam("uid") String uid) {
        return complexMapper.getByUser(uid);
    }

    @GetMapping("/getByGood")
    public List<TestModel> getByGood(@RequestParam("gid") String gid) {
        return complexMapper.getByGood(gid);
    }

    @GetMapping("/get")
    public List<TestModel> get(@RequestParam("uid") String uid, @RequestParam("gid") String gid) {
        return complexMapper.get(uid, gid);
    }

}
