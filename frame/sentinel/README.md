# sentinel
面向分布式、多语言异构化服务架构的流量治理组件
[官网](https://sentinelguard.io/zh-cn/)

## 使用
### 异常
```java
public boolean testFor(String resource) {
    try (Entry entry = SphU.entry(resource)) {
        return true;
    } catch (BlockException e) {
        return false;
    }
}
```
### 布尔值
```java
private boolean testFor(String resource) {
    return SphO.entry(resource);
}
```
### 注解
`@SentinelResource("resource")`
### 异步

## 规则
### 访问控制
黑白名单
[AuthorityRuleTest.java](src/main/java/com/example/rule/AuthorityRuleTest.java)
### 流量控制
根据访问流量(QPS)限制
[FlowRuleTest.java](src/main/java/com/example/rule/FlowRuleTest.java)
### 熔断降级
根据超时比例、异常比例、异常数熔断
[DegradeRuleTest.java](src/main/java/com/example/rule/DegradeRuleTest.java)
### 系统保护
根据系统状态限制
### _热点规则_