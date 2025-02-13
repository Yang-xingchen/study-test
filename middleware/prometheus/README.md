# prometheus
[官网](https://prometheus.io/)
[管理页面](http://localhost:9090/)

## 安装
解压即可

## 启动
```shell
nohup ./prometheus > /dev/null 2>&1 &
```

# node_exporter
系统状态监控

## 安装
解压即可

## 启动
```shell
nohup ./node_exporter > /dev/null 2>&1 &
```

## 配置
1. 到`prometheus`根目录
2. 编辑配置文件`vim prometheus.yml`
    ```
   scrape_configs:
     # (省略其他配置)
     - job_name: "node"
       static_configs:
         - targets: ["192.168.31.201:9100","192.168.31.202:9100","192.168.31.203:9100"]
   ```
3. 重启`prometheus`

# mysqld_exporter
MySQL监控

## 安装
1. 解压
2. 配置账号 `vim .my.cnf`
    ```
   [client]
   user = root
   password = 123456
   ```

## 启动
```shell
nohup ./mysqld_exporter > /dev/null 2>&1 &
```

## 配置
1. 到`prometheus`根目录
2. 编辑配置文件`vim prometheus.yml`
    ```
   scrape_configs:
     # (省略其他配置)
     - job_name: "mysql"
       static_configs:
         - targets: ["192.168.31.201:9104"]
   ```
3. 重启`prometheus`

# Spring

## 配置
- spring
   ```yaml
  management:
    endpoints:
      web:
        exposure:
          include: 'prometheus, health, info'
    metrics:
      export:
        prometheus:
          enabled: true
  ```
- Prometheus
    ```
   scrape_configs:
     # (省略其他配置)
     - job_name: "mysql"
       metrics_path: "/actuator/prometheus"
       static_configs:
         - targets: ["192.168.31.218:8080"]
   ```

# Grafana
[官网](https://grafana.com/zh-cn/grafana/)
[管理页面](http://localhost:3000/)

## 安装
解压即可

## 启动
```shell
nohup bin/grafana server > /dev/null 2>&1 &
```

## 其他说明
- 第一次登录账号`admin`密码`admin`
- 个人资料可改语言
- 链接->数据源添加`prometheus`。`HTTP method`选择`GET`