# 安装

## JDK
[安装](../../base/JDK安装.md)

## spark
1. 解压
2. 到根目录
3. 复制示例配置 ```cp conf/spark-env.sh.template conf/spark-env.sh```
4. 编辑配置 ```vim conf/spark-env.sh```
    ```
   # YARN 路径
   YARN_CONF_DIR=
   ```

## 历史服务器
1. (完成spark 2)
2. 复制配置 ```cp conf/spark-defaults.conf.template conf/spark-defaults.conf```
3. 编辑配置 ```vim conf/spark-env.sh```
    ```
   spark.eventLog.enabled           true
   spark.eventLog.dir               hdfs://namenode:8020/directory
   spark.yarn.historyServer.address=namenode:18080
   spark.history.ui.port=18080
   ```
4. 编辑配置 ```vim conf/spark-env.sh```
   ```
   SPARK_HISTORY_OPTS="-Dspark.history.ui.port=18080 -Dspark.history.fs.logDirectory=hdfs://zk1:8020/sparkHistory -Dspark.history.retainedApplications=30
   ```
   - spark.history.ui.port: WEB访问端口号
   - spark.history.fs.logDirectory: 存储路径
   - spark.history.retainedApplications: 存储任务数量
5. hdfs创建目录 `directory`

### 命令
- 提交任务 ```bin/spark-submit --class xxx.Xxx --master local/yarn xxx.jar args```
- 启动历史服务 ```sbin/start-history-server.sh```
- 关闭历史服务 ```sbin/stop-history-server.sh```
