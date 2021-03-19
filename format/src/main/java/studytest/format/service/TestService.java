package studytest.format.service;

import org.springframework.stereotype.Service;
import studytest.format.model.TestModel;

@Service
public class TestService {


    public TestModel getByStr(String s) {
        String[] split = s.split("-");
        return new TestModel().setType(Integer.parseInt(split[0])).setSrc(s).setValue(split[1]);
    }

}
