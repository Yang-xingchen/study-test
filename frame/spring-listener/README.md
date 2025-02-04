# spring 事件/生命周期

## 可用类型
### org.springframework.boot.SpringApplicationRunListener
> 处理程序启动相关生命周期

需要在[spring.factories](src/main/resources/META-INF/spring.factories)中定义。

[Listener.java](src/main/java/springstudy/event/Listener.java)

### org.springframework.context.ApplicationListener
> 处理程序运行相关生命周期

可以使用`@Component`配置。

泛型可限定接收事件类型

[RunListener.java](src/main/java/springstudy/event/RunListener.java)

### org.springframework.context.SmartLifecycle
> 处理生命周期，用处不大

可以使用`@Component`配置

[MyLifecycle.java](src/main/java/springstudy/event/MyLifecycle.java)

## 执行顺序
1. (系统)创建`ConfigurableBootstrapContext`
2. 创建`SpringApplicationRunListener`
3. `SpringApplicationRunListener#starting`
4. `SpringApplicationRunListener#environmentPrepared`
5. (系统)打印`Banner`
6. `SpringApplicationRunListener#contextPrepared`
7. (系统)关闭`ConfigurableBootstrapContext`
8. `SpringApplicationRunListener#contextLoaded`
9. `BeanFactoryPostProcessor`
10. (系统)初始化tomcat
11. **初始化其他组件(其他框架、用户组件)**
12. 创建`ApplicationListener`
13. `ApplicationListener`接收事件`ServletWebServerInitializedEvent`
14. `SmartLifecycle#start`
15. `ApplicationListener`接收事件`ContextRefreshedEvent`
16. `ApplicationListener`接收事件`ApplicationStartedEvent`
17. `ApplicationListener`接收事件`AvailabilityChangeEvent`, state: `CORRECT`
18. `SpringApplicationRunListener#started`
19. 执行`CommandLineRunner`
20. `ApplicationListener`接收事件`ApplicationReadyEvent`
21. `ApplicationListener`接收事件`AvailabilityChangeEvent`, state: `ACCEPTING_TRAFFIC`
22. `SpringApplicationRunListener#ready`
23. (系统)`SpringApplication#run`方法结束
24. *============================== 程序运行 ==============================*
25. (用户)发送http请求结束后: `ApplicationListener`接收事件`ServletRequestHandledEvent`
26. (用户)发布自定义事件时: `ApplicationListener`接收事件
27. *============================== 程序退出 ==============================*
28. `ApplicationListener`接收事件`AvailabilityChangeEvent`, state: `REFUSING_TRAFFIC`
29. `ApplicationListener`接收事件`ContextClosedEvent`
30. `SmartLifecycle#stop`
31. 执行`DisposableBean#destroy`