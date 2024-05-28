# 安装

## JDK
1. 解压
2. 配置环境 ```vim /etc/profile```
3. 应用配置 ```source /etc/profile```

## zookeeper
1. 解压
2. 到根目录
3. 复制示例配置 ```cp conf/zoo_sample.cfg conf/zoo.cfg```
4. 编辑配置 ```vim conf/zoo.cfg```
    ```
   # 数据保存目录
   dataDir=
   # 集群地址
   server.1=ip:2888:3888
   server.2=ip:2888:3888
   ...
   ```
5. 创建数据目录 ```mkdir data```
6. 集群配置 ```echo 1 > data/myid``` (在不同节点配置不同id，对应集群地址里配置id)

### 命令
- 启动 ```bin/zkServer.sh start```
- 查看状态 ```bin/zkServer.sh status```
- 关闭 ```bin/zkServer.sh stop```

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