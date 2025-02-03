package springstudy.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(EventApplication.class)
				.run(args);
		log.info("main end");
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> log.info("CommandLineRunner...");
	}

}
