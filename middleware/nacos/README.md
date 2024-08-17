# 官网地址
[nacos](https://nacos.io/)
# 安装

## JDK
[安装](../../base/JDK安装.md)

## MySQL
1. [安装](../mysql/README.md)
2. 初始化表 [schema.sql](https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql)

## Nacos

1. 下载: https://nacos.io/download/nacos-server/
2. 解压: ```unzip nacos-server-2.4.1.zip -d /opt/module/```
3. 到根目录
4. 复制集群配置文件 ```cp conf/cluster.conf.example conf/cluster.conf```
5. 编辑集群配置文件 ```vim conf/cluster.conf```
    ```
   ip1:port
   ip2:port
   ```
6. 编辑应用配置 ```vim conf/application.properties```
    ```
    # 数据库
    spring.sql.init.platform=mysql
    db.num=1
    db.url.0=jdbc:mysql://${mysql_host}:${mysql_port}/${nacos_database}?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&allowPublicKeyRetrieval=true
    db.user=${mysql_user}
    db.password=${mysql_password}
    # 鉴权
    nacos.core.auth.enabled=true
    nacos.core.auth.server.identity.key=
    nacos.core.auth.server.identity.value=
    nacos.core.auth.plugin.nacos.token.secret.key=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=
   ```
7. 设置密码(账号nacos) ```curl -X POST 'http://127.0.0.1:8848/nacos/v1/auth/users/admin' -d 'password='```

## 其他
- 启动 ```bin/startup.sh```
- 关闭 ```bin/shutdown.sh```
- 管理平台 http://127.0.0.1:8848/nacos

## 异常处理
### 密码过短
调整`nacos.core.auth.plugin.nacos.token.secret.key`参数，增长超过32位

### Unable to make field private byte java.lang.StackTraceElement.format accessible: module java.base does not "opens java.lang" to unnamed module @36f6e879
高版本JDK不支持
[issue](https://github.com/alibaba/nacos/issues/12498)

编辑启动脚本 `vim bin/startup.sh` 增加JAVA_HOME配置，更改为JDK8路径

### Public Key Retrieval is not allowed
数据库SSL/TSL配置问题
数据库连接参数添加`allowPublicKeyRetrieval=true`