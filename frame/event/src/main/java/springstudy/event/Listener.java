package springstudy.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Slf4j
@Component
public class Listener implements ApplicationListener<ApplicationEvent> {

    public Listener() {
        log.info("created Listener...");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        switch (event) {
            case ServletWebServerInitializedEvent e -> log.info("[event]ServletWebServerInitializedEvent: {}", e.getWebServer().getPort());
            case ContextRefreshedEvent e -> log.info("[event]ContextRefreshedEvent: {}", e.getApplicationContext());
            case ApplicationStartedEvent e -> log.info("[event]ApplicationStartedEvent: {}", e.getApplicationContext());
            case AvailabilityChangeEvent<?> e -> log.info("[event]AvailabilityChangeEvent: {}", e.getState());
            case ApplicationReadyEvent e -> log.info("[event]ApplicationReadyEvent: {}", e.getApplicationContext());
            case ServletRequestHandledEvent e -> log.info("[event]ServletRequestHandledEvent: {}", e.getRequestUrl());
            case Controller.CustomEvent e -> log.info("[event]CustomEvent: {}", e.getSource());
            case ContextClosedEvent e -> log.info("[event]ContextClosedEvent: {}", e.getApplicationContext());
            default -> log.info("[event]{}: {}", event.getClass(), event);
        }
    }

}
