package studytest.format.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import studytest.format.model.TestModel;

@RestController
public class TestController {


    @GetMapping("/")
    public TestModel test(@RequestParam TestModel testModel) {
        return testModel;
    }


}
