package com.example.nacos.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class NacosDubboProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosDubboProviderApplication.class, args);
	}

}
