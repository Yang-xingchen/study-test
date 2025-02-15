# Pulsar
[官网](https://pulsar.apache.org/)

## 安装
[zookeeper](../zookeeper/README.md)
1. 解压
2. 到根目录
3. 启动zookeeper
4. 初始化元数据(只执行一次)
    ```shell
   bin/pulsar initialize-cluster-metadata \
    --cluster pulsar-cluster \
    --metadata-store zk:zk1:2181,zk2:2181,zk3:2181 \
    --configuration-metadata-store zk:zk1:2181,zk2:2181,zk3:2181 \
    --web-service-url http://zk1:8081,zk2:8081,zk3:8081 \
    --broker-service-url pulsar://zk1:6650,zk2:6650,zk3:6650 
   ```
5. 配置bookKeeper `vim conf/bookkeeper.conf`
    ```
   metadataServiceUri=zk://zk1:2181;zk2:2181;zk3:2181/ledgers
   ```
6. 配置broker `vim conf/broker.conf`
    ```
   metadataStoreUrl=zk:zk1:2181,zk2:2181,zk3:2181
   configurationMetadataStoreUrl=zk:zk1:2181,zk2:2181,zk3:2181
   clusterName=pulsar-cluster
   brokerServicePort=6650
   webServicePort=8081
   ```
7. 配置客户端 `vim conf/client.conf`
    ```
   webServiceUrl=http://zk1:8081,zk2:8081,zk3:8081
   brokerServiceurl=pulsar://zk1:6650,zk2:6650,zk3:6650
   ```

**NOTE: 首次启动可能需要前台启动，原因不明**

## 命令
启动bookie
```shell
bin/pulsar-daemon start bookie
```
启动broker
```shell
bin/pulsar-daemon start broker
```
关闭
```shell
bin/pulsar-daemon stop bookie
bin/pulsar-daemon stop broker
```
创建主题
```shell
bin/pulsar-admin topics create persistent://public/default/topic
```

