package com.example;

import com.example.annotation.AnnotationMain;
import com.example.base.BaseMain;
import com.example.plugin.Plugin;
import com.example.plugin.PluginMain;
import com.example.result.ResultMain;
import org.apache.ibatis.plugin.Interceptor;
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

    @Bean
    public CommandLineRunner result() {
        return new ResultMain();
    }

    @Bean
    public Interceptor interceptor() {
        return new Plugin();
    }

    @Bean
    public CommandLineRunner plugin() {
        return new PluginMain();
    }

}
