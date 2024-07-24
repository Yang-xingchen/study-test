# 安装

## JDK
[安装](../../base/JDK安装.md)

## zookeeper
[安装](../zookeeper/README.md)

## kafka
1. 解压
2. 到根目录
3. 编辑配置 ```vim kafka/config/server.properties```
   ```
   # 节点id
   broker.id=
   # 本机地址
   advertised.listeners=PLAINTEXT://IP:9092
   # 数据地址
   log.dirs=
   # zookeeper集群地址
   zookeeper.connect=ip:2181,ip:2181
   ```
4. 创建数据目录 ```mkdir data```

### 命令
- 启动(保证zookeeper已启动) ```bin/kafka-server-start.sh -daemon config/server.properties```
- 关闭 ```bin/kafka-server-stop.sh```