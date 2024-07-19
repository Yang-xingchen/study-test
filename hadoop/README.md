# 安装

## JDK
1. 解压
2. 配置环境 ```vim /etc/profile```
3. 应用配置 ```source /etc/profile```

## hadoop
- namenode: IP1
- secondary-namenode: IP2
- resourcemanager: IP3

1. 解压
2. 到根目录
3. 配置
    1. ```etc/hadoop/core-site.xml```
    ```xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://IP1:8020</value>
        </property>
        <property>
            <name>hadoop.tmp.dir</name>
            <value>/opt/module/hadoop/data</value>
        </property>
        <property>
            <name>hadoop.http.staticuser.user</name>
            <value>root</value>
        </property>
    </configuration>
   ```
   2. ```etc/hadoop/hdfs-site.xml```
   ```xml
    <configuration>
        <property>
            <name>dfs.namenode.http-address</name>
            <value>IP1:9870</value>
        </property>
        <property>
            <name>dfs.namenode.secondary.http-address</name>
            <value>IP2:9870</value>
        </property>
    </configuration>   
   ```
   3. ```etc/hadoop/yarn-site.xml```
   ```xml
    <configuration>
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <name>yarn.resourcemanager.hostname</name>
            <value>IP3</value>
        </property>
    </configuration>   
   ```
   4. ```etc/hadoop/mapred-site.xml```
   ```xml
    <configuration>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>   
   ```
   5. ```etc/hadoop/workers```
   ```
   IP1
   IP2
   IP3
   ```
4. 初始化(仅在namenode执行) ```hdfs namenode -format```

### 其他
启动HDFS ```sbin/start-dfs.sh```
启动YARN(IP3) ```sbin/start-yarn.sh```
网页地址: http://IP1:9870/

# 报错说明
## ERROR: Attempting to operate on hdfs namenode as root
无ssh访问权限导致。
1. 生成: ```ssh-keygen -t rsa```
2. 分发: ```ssh-copy-id IP```
3. 配置环境: ```vim /etc/profile```
   ```
   export HDFS_NAMENODE_USER=root
   export HDFS_DATANODE_USER=root
   export HDFS_SECONDARYNAMENODE_USER=root
   export YARN_RESOURCEMANAGER_USER=root
   export YARN_NODEMANAGER_USER=root
   ```
4. 应用配置 ```source /etc/profile```

## ERROR: JAVA_HOME is not set and could not be found.
JAVA配置未生效。编辑配置: ```vim etc/hadoop/hadoop-env.sh```
```
export JAVA_HOME=
```

## IP1:9870 页面报错Failed to retrieve data from /webhdfs/v1
JDK版本过高。降级为JDK8即可。
