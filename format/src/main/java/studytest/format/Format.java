package studytest.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import studytest.format.model.TestModel;
import studytest.format.service.TestService;

import java.text.ParseException;
import java.util.Locale;


public class Format implements Formatter<TestModel> {

    @Autowired
    private TestService testService;

    public Format setTestService(TestService testService) {
        this.testService = testService;
        return this;
    }

    @Override
    public TestModel parse(String text, Locale locale) throws ParseException {
        return testService.getByStr(text);
    }

    @Override
    public String print(TestModel object, Locale locale) {
        return object.toString();
    }
}
