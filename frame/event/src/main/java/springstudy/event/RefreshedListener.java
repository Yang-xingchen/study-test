package springstudy.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("======================");
        // a class
        // b class
        event.getApplicationContext().getBeansWithAnnotation(AnnotationComponent.class).forEach((s, o) -> {
            log.info(s, o.getClass().getAnnotation(AnnotationComponent.class).value());
        });
    }
}
