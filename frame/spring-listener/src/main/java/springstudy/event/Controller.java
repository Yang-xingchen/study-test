package springstudy.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springstudy.event.component.B;

@RestController
public class Controller {

    @Autowired
    private B b;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/test")
    public String test() {
        applicationContext.publishEvent(new CustomEvent(this));
        return b.test();
    }

    public static class CustomEvent extends ApplicationEvent {

        public CustomEvent(Object source) {
            super(source);
        }

    }

}
