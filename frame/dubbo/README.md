# dubbo
[官网](https://cn.dubbo.apache.org/zh-cn/)

# 使用(nacos)
- [api](../../middleware/nacos/nacos-dubbo-api/src/main/java/com/example/nacos/server/Server.java)
- [provider](../../middleware/nacos/nacos-dubbo-provider/src/main/java/com/example/nacos/provider/ServerImpl.java)
- [consumer](../../middleware/nacos/nacos-dubbo-consumer/src/main/java/com/example/nacos/consumer/TestController.java)

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
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            <version>${spring-cloud-alibaba.version}</version>
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
## 单协议
1. 提供者`dubbo`配置。[application.yaml](nacos-dubbo-provider/src/main/resources/application.yaml)
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
2. 消费者`dubbo`配置。[application.yaml](nacos-dubbo-consumer/src/main/resources/application.yaml)
   ```yaml
   dubbo:
      application:
         name: ${spring.application.name}
         qos-enable: false
      registry:
         address: nacos://192.168.31.201:8848?username=nacos&password=123456
   ```
3. 提供者消费者启动类添加`@EnableDubbo`注解。
4. 提供者添加`@DubboService`注解。[ServerImpl.java](nacos-dubbo-provider/src/main/java/com/example/nacos/provider/ServerImpl.java)
5. 消费者使用`@DubboReference`注入。[TestController.java](nacos-dubbo-consumer/src/main/java/com/example/nacos/consumer/TestController.java)

## 多协议
1. 提供者`dubbo`配置。[application.yaml](nacos-dubbo-provider/src/main/resources/application.yaml)
   ```yaml
   dubbo:
      application:
         name: ${spring.application.name}
         qos-enable: false
      protocols:
         dubbo:
            name: dubbo
            port: 50051
         rest:
            name: tri
            port: 50052
      registry:
         address: nacos://192.168.31.201:8848?username=nacos&password=123456
   ```
2. 提供者`@DubboService`注解添加`protocol`属性。内容为`dubbo.protocols`下可用id。

### dubbo
[dubbo协议](https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/protocol/dubbo/)
1. 消费者配置同单协议
2. 消费者`@DubboReference`注解添加`protocol`属性。内容为提供者`dubbo.protocols`下可用id。

### tri
[trilpe协议](https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/protocol/triple/)
支持REST。
1. 接口添加REST相关注解。[Server.java](nacos-dubbo-api/src/main/java/com/example/nacos/server/Server.java)
2. 消费者使用REST风格的HTTP访问即可。
