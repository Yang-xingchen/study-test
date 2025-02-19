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
1. 添加建造者bean配置
    ```java
    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuild() {
        return RestClient
                .builder();
    }
    ```
2. 添加 `RestClient` bean配置
    ```java
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
   ```

## HttpExchange
1. 根据基于 `RestTemplate` 或 `RestClient`， 创建对应bean配置。需本身可实现`LoadBalanced`
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
   
## Dubbo
1. 添加依赖dubbo管理。
   ```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-bom</artifactId>
                <version>${dubbo.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
   ```
2. 添加依赖
   ```xml
   <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>nacos-dubbo-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            <version>${nacos.version}</version>
        </dependency>
        <!-- dubbo -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-nacos-spring-boot-starter</artifactId>
        </dependency>
   </dependencies>
   ```
3. 提供者`dubbo`配置。[application.yaml](nacos-dubbo-provider/src/main/resources/application.yaml)
   ```yaml
   dubbo:
      application:
         name: ${spring.application.name}
         qos-enable: false
      protocol:
         name: tri
         port: 50052
      registry:
         address: nacos://192.168.31.201:8848?username=nacos&password=123456
   ```
4. 消费者`dubbo`配置。[application.yaml](nacos-dubbo-consumer/src/main/resources/application.yaml)
   ```yaml
   dubbo:
      application:
         name: ${spring.application.name}
         qos-enable: false
      registry:
         address: nacos://192.168.31.201:8848?username=nacos&password=123456
   ```
5. 提供者消费者启动类添加`@EnableDubbo`注解。
6. 提供者添加`@DubboService`注解。[ServerImpl.java](nacos-dubbo-provider/src/main/java/com/example/nacos/provider/ServerImpl.java)
7. 消费者使用`@DubboReference`注入。[TestController.java](nacos-dubbo-consumer/src/main/java/com/example/nacos/consumer/TestController.java)