# seata
[官网](https://seata.apache.org/zh-cn/)

# 安装
## JDK
[安装](../../base/JDK安装.md)

## seata
解压即可

## 其他
- 启动 `bin/seata-server.sh`
- 管理平台 http://192.168.31.201:7091/
- 默认用户名密码: `seata` `seata`

# 事务模式
## AT

**需要在对应数据库下创建`undo_log`表**
```
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

## TCC

## Saga

## XA

# 微服务适配
## Dubbo
- [api](./seata-dubbo/seata-dubbo-api/src/main/java/com/example/seata/server/Server.java)
- [provider](./seata-dubbo/seata-dubbo-provider/src/main/java/com/example/seata/provider/SeataDubboProviderApplication.java)
- [consumer](./seata-dubbo/seata-dubbo-consumer/src/main/java/com/example/seata/consumer/SeataDubboConsumerApplication.java)

## Spring
- [provider](./seata-spring/seata-spring-provider/src/main/java/com/example/seata/provider/SeataSpringProviderApplication.java)
- [consumer](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/SeataSpringConsumerApplication.java)
  - [RestTemplate](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/service/RestTemplateServerImpl.java)
    ```java
    @Bean
    public RestTemplate restTemplate(LoadBalancerInterceptor loadBalancer, SeataRestTemplateInterceptor seata) {
        return new RestTemplateBuilder()
				.interceptors(loadBalancer, seata)
				.build();
    }
    ```
  - [RestClient](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/service/RestClientServerImpl.java)
    ```java
    @Bean
    public RestClient restClient(LoadBalancerInterceptor loadBalancer, SeataRestTemplateInterceptor seata) {
        return RestClient.builder()
				.requestInterceptors(interceptors -> {
					interceptors.add(loadBalancer);
					interceptors.add(seata);
				})
				.build();
    }
    ```
  - [WebClient](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/service/WebClientServerImpl.java)
    ```java
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
    ```
  - [Feign](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/service/FeignServerImpl.java)
  - [HttpExchange](./seata-spring/seata-spring-consumer/src/main/java/com/example/seata/consumer/service/ExchangeServerImpl.java)

