package springstudy.event.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import springstudy.event.AnnotationComponent;

@Slf4j
@AnnotationComponent("b class")
public class B implements DisposableBean {

    @Autowired
    private A a;

    public B() {
        log.info("created B");
    }

    public String test() {
        return a == null ? "null" : a.toString();
    }

    @Override
    public void destroy() throws Exception {
        log.info("destroy B");
    }

}
