# 安装

## JDK
[安装](../../base/JDK安装.md)

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
