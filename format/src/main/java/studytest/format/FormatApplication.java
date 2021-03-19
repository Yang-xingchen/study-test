package studytest.format;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import studytest.format.service.TestService;

@SpringBootApplication
public class FormatApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(FormatApplication.class, args);
	}

    // @Bean
    // public TestService testService() {
    //     return new TestService();
    // }

    @Bean
    public Format format() {
        return new Format();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
	    registry.addFormatter(format());
    }
}
