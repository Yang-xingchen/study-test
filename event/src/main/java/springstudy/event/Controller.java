package springstudy.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springstudy.event.component.B;

@RestController
public class Controller {

    @Autowired
    private B b;

    @GetMapping("/test")
    public String test() {
        return b.test();
    }

}
