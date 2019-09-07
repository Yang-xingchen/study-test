package spring;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@Log
@EnableAspectJAutoProxy
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("spring");
        log.info(applicationContext.toString());
        A a = applicationContext.getBean(A.class);
        a.print();
    }


}
