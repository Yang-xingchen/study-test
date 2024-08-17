# 配置中心
1. 配置dataId
    ```
   spring:
        config:
            import: optional:nacos:dataId
   ```
2. 控制台添加配置 `Data ID` 为上述配置dataId, 无需后缀
3. 如果需要刷新配置，则在类上添加`@RefreshScope`。当配置发布，该类会重新创建。