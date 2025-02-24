package com.example.seata.consumer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class SeataDubboConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeataDubboConsumerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commit(TestService testService) {
		return args -> {
			Long id = testService.commit();
			testService.check("commit", id, 2);
		};
	}

	@Bean
	public CommandLineRunner rollback(TestService testService) {
		return args -> {
			Long id;
			try {
				id = testService.rollback();
			} catch (TestException e) {
				id = e.getId();
			}
			testService.check("rollback", id, null);
		};
	}

}
