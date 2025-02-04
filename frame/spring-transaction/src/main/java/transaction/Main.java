package transaction;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@AllArgsConstructor
@ComponentScan("transaction")
@Slf4j
public class Main implements CommandLineRunner {

    @Autowired
    public final ModelServer modelServer;
    @Autowired
    private final MultiService multiService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Main.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        modelServer.clear();
//        base();
        multi();
    }

    private void base() {
        try {
            modelServer.defaultTransaction();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OITrTh();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OTrITh();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OISTrTh();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OTrThI();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OTryISTrNewTh();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OISTrNewTh();
        } catch (Exception ignored) {
        }
        try {
            modelServer.OTryITh();
        } catch (Exception ignored) {
        }
        log.info(modelServer.count("A") == 0 ? "OK" : modelServer.count("A") + "");
        log.info(modelServer.count("B") == 2 ? "OK" : modelServer.count("B") + "");
        log.info(modelServer.count("C") == 0 ? "OK" : modelServer.count("C") + "");
        log.info(modelServer.count("D") == 1 ? "OK" : modelServer.count("D") + "");
        log.info(modelServer.count("E") == 0 ? "OK" : modelServer.count("E") + "");
        log.info(modelServer.count("F") == 1 ? "OK" : modelServer.count("F") + "");
        log.info(modelServer.count("G") == 0 ? "OK" : modelServer.count("F") + "");
        log.info(modelServer.count("H") == 0 ? "OK" : modelServer.count("F") + "");
    }

    private void multi() {
        try {
            multiService.normal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            multiService.exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            multiService.multiRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
