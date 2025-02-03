package springstudy.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyLifecycle implements SmartLifecycle {

    private boolean isRunning = false;

    @Override
    public void start() {
        log.info("Lifecycle start");
        isRunning = true;
    }

    @Override
    public void stop() {
        log.info("Lifecycle stop");
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
