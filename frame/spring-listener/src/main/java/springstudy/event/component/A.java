package springstudy.event.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import springstudy.event.AnnotationComponent;

@Slf4j
@AnnotationComponent("a class")
public class A implements DisposableBean {

    public A() {
        log.info("created A");
    }

    @Override
    public void destroy() throws Exception {
        log.info("destroy A");
    }

}
