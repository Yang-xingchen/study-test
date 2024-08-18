# 配置中心
1. 配置dataId和nacos地址
    ```
   spring:
      config:
         import: optional:nacos:dataId
      cloud:
         nacos:
            config:
               server-addr: 192.168.31.201:8848
               file-extension: yaml
               username: nacos
               password: 123456
   ```
2. 控制台添加配置 `Data ID` 为上述配置dataId, 无需后缀
3. 如果需要刷新配置，则在类上添加`@RefreshScope`。当配置发布，该类会重新创建。