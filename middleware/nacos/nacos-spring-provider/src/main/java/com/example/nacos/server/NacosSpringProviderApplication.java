package com.example.nacos.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosSpringProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosSpringProviderApplication.class, args);
	}

}
