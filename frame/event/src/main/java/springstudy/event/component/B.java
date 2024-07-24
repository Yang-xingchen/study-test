package springstudy.event.component;

import org.springframework.beans.factory.annotation.Autowired;
import springstudy.event.AnnotationComponent;

@AnnotationComponent("b class")
public class B {

    @Autowired
    private A a;

    public String test() {
        return a == null ? "null" : a.toString();
    }
}
