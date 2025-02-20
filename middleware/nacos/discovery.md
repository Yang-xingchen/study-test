# 服务发现
1. 配置dataId和nacos地址
    ```yaml
   spring:
      cloud:
         nacos:
            discovery:
               server-addr: 192.168.31.201:8848
               file-extension: yaml
               username: nacos
               password: 123456
   ```
2. 启动类添加 `@EnableDiscoveryClient`

## OpenFeign
1. [pom.xml](nacos-consumer/pom.xml)添加依赖
    ```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
   ```
2. 启动类添加 `@EnableFeignClients`
3. 创建类 [ServerClientByFeign.java](nacos-spring-consumer/src/main/java/com/example/nacos/consumer/ServerClientByFeign.java)
   1. `@FeignClient`内填写服务名称, 为对应服务的 `spring.application.name`
   2. 添加对应方法

## RestTemplate
1. 添加bean配置
    ```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
   ```

## RestClient
1. 添加bean配置
    ```java
   @Bean
   public RestClient restClient(LoadBalancerInterceptor interceptor) {
      return RestClient.builder()
				.requestInterceptor(interceptor)
				.build();
   }
   ```

## WebClient
1. 添加bean配置
   ```java
   @Bean
   public WebClient webClient(ReactorLoadBalancerExchangeFilterFunction filterFunction) {
      return WebClient.builder()
				.filter(filterFunction)
				.build();
   }
   ```

## HttpExchange
1. 根据基于 `RestTemplate`、`RestClient` 或 `WebClient`， 创建对应bean配置。需本身可实现`LoadBalanced`
2. 创建类 [ServerClientByExchange.java](nacos-spring-consumer/src/main/java/com/example/nacos/consumer/ServerClientByExchange.java)
   1. `@HttpExchange`填写服务地址， 如: `http://serverName/`
   2. 添加对应方法
3. 添加客户端bean配置
    > 基于 `RestTemplate`
    ```java
    @Bean
    public ServerClientByExchange serverClientByExchange(RestTemplate restTemplate) {
        RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ServerClientByExchange.class);
    }
   ```
   > 基于 `RestClient`
   ```java
    @Bean
    public ServerClientByExchange serverClientByExchange(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ServerClientByExchange.class);
    }
   ```
   > 基于 `WebClient`
   ```java
    @Bean
    public ServerClientByExchange serverClientByExchange(WebClient webClient) {
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ServerClientByExchange.class);
    }
   ```
   
## Dubbo
[README.md](../../frame/dubbo/README.md)