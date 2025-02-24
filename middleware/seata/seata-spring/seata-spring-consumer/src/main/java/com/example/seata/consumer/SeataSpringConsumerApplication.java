package com.example.seata.consumer;

import com.alibaba.cloud.seata.rest.SeataRestTemplateInterceptor;
import com.example.seata.consumer.service.ExchangeServerClient;
import com.example.seata.consumer.service.ExchangeServerImpl;
import com.example.seata.consumer.service.Server;
import org.apache.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class SeataSpringConsumerApplication {

	public static final String PROVIDER_NAME = "seataSpringProvider";
	private static final Logger log = LoggerFactory.getLogger(SeataSpringConsumerApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(SeataSpringConsumerApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean
	public Server restClientServer() {
//		return new RestTemplateServerImpl();
//		return new RestClientServerImpl();
//		return new WebClientServerImpl();
//		return new FeiginServerImpl();
		return new ExchangeServerImpl();
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
				log.info("rollback", e);
				id = e.getId();
			}
			testService.check("rollback", id, null);
		};
	}

	@Bean
	public RestTemplate restTemplate(LoadBalancerInterceptor loadBalancer, SeataRestTemplateInterceptor seata) {
		return new RestTemplateBuilder()
				.interceptors(loadBalancer, seata)
				.build();
	}

	@Bean
	public RestClient restClient(LoadBalancerInterceptor loadBalancer, SeataRestTemplateInterceptor seata) {
		return RestClient.builder()
				.requestInterceptors(interceptors -> {
					interceptors.add(loadBalancer);
					interceptors.add(seata);
				})
				.build();
	}

	@Bean
	public WebClient webClient(ReactorLoadBalancerExchangeFilterFunction filter) {
		return WebClient.builder()
				.filter(filter)
				.filter((request, next) -> {
					String xid = RootContext.getXID();
					if (StringUtils.hasLength(xid)) {
						request = ClientRequest.from(request)
								.headers(httpHeaders -> httpHeaders.add(RootContext.KEY_XID, xid))
								.build();
					}
					return next.exchange(request);
				})
				.build();
	}


//	@Bean
//	public ExchangeServerClient exchangeServerClient(RestTemplate restTemplate) {
//		RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
//		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//		return factory.createClient(ExchangeServerClient.class);
//	}
//
//	@Bean
//	public ExchangeServerClient exchangeServerClient(RestClient restClient) {
//		RestClientAdapter adapter = RestClientAdapter.create(restClient);
//		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//		return factory.createClient(ExchangeServerClient.class);
//	}

	@Bean
	public ExchangeServerClient exchangeServerClient(WebClient webClient) {
		WebClientAdapter adapter = WebClientAdapter.create(webClient);
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
		return factory.createClient(ExchangeServerClient.class);
	}

}
