package spring.ioc;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Log
@Component
public class B {

    void printf(){
        log.info("class B print");
    }

}
