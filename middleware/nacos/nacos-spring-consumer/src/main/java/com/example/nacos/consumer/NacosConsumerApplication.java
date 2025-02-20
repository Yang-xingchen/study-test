package com.example.nacos.consumer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class NacosConsumerApplication {

	public static final String PROVIDER_NAME = "nacosSpringProvider";

	public static void main(String[] args) {
		new SpringApplicationBuilder(NacosConsumerApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestClient restClient(LoadBalancerInterceptor interceptor) {
		return RestClient.builder()
				.requestInterceptor(interceptor)
				.build();
	}

	@Bean
	public WebClient webClient(ReactorLoadBalancerExchangeFilterFunction filter) {
		return WebClient.builder()
				.filter(filter)
				.build();
	}

//	@Bean
//	public ServerClientByExchange serverClientByExchange(RestTemplate restTemplate) {
//		RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
//		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//		return factory.createClient(ServerClientByExchange.class);
//	}
//
//	@Bean
//	public ServerClientByExchange serverClientByExchange(RestClient restClient) {
//		RestClientAdapter adapter = RestClientAdapter.create(restClient);
//		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//		return factory.createClient(ServerClientByExchange.class);
//	}

	@Bean
	public ServerClientByExchange serverClientByExchange(WebClient webClient) {
		WebClientAdapter adapter = WebClientAdapter.create(webClient);
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(ServerClientByExchange.class);
	}

}
