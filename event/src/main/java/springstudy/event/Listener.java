package springstudy.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Listener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent e) {
            log.info("ApplicationStartedEvent: " + e.getApplicationContext());
        } else if (event instanceof AvailabilityChangeEvent e) {
            log.info("AvailabilityChangeEvent: " + e.getState());
        } else if (event instanceof ApplicationReadyEvent e) {
            log.info("ApplicationReadyEvent: " + e.getApplicationContext());
        } else if (event instanceof ContextClosedEvent e) {
            log.info("ContextClosedEvent" + e.getApplicationContext());
        } else {
            log.info(event.getClass() + ": " + event);
        }
    }
}
