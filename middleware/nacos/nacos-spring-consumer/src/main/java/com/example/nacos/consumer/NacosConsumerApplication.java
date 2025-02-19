package com.example.nacos.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class NacosConsumerApplication {

	public static final String PROVIDER_NAME = "nacosSpringProvider";

	public static void main(String[] args) {
		SpringApplication.run(NacosConsumerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@LoadBalanced
	public RestClient.Builder restClientBuild() {
		return RestClient
				.builder();
	}

	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		return builder.build();
	}

//	@Bean
//	public ServerClientByExchange serverClientByExchange(RestTemplate restTemplate) {
//		RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
//		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//		return factory.createClient(ServerClientByExchange.class);
//	}

	@Bean
	public ServerClientByExchange serverClientByExchange(RestClient restClient) {
		RestClientAdapter adapter = RestClientAdapter.create(restClient);
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(ServerClientByExchange.class);
	}

}
