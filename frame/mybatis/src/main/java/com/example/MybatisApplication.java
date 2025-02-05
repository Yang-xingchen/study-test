package com.example;

import com.example.annotation.AnnotationMain;
import com.example.base.BaseMain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MybatisApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MybatisApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public CommandLineRunner base() {
        return new BaseMain();
    }

    @Bean
    public CommandLineRunner annotation() {
        return new AnnotationMain();
    }

}
