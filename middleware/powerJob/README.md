# PowerJob
[官网](http://www.powerjob.tech/)
[文档](https://www.yuque.com/powerjob/guidence)

# 安装

## JDK
[安装](../../base/JDK安装.md)

## PowerJob
1. 添加数据库 ```CREATE DATABASE IF NOT EXISTS `powerjob-product` DEFAULT CHARSET utf8mb4;```
2. 下载源码 ```git clone https://github.com/PowerJob/PowerJob.git```
3. 修改配置文件 `powerjob-server\powerjob-server-starter\src\main\resources\application-product.properties`, 调整MySql链接配置
4. 打包 ```mvn clean package -U -Pdev -DskipTests```

# 启动
```shell
nohup java -jar powerjob-server-starter-5.1.1.jar > /dev/null 2>&1 &
```

# 初始化
1. 运行，查看日志, 获取默认密码
   ```
   [SystemInitializeService] [S1] create default PWJB user by request: ModifyUserInfoRequest(id=null, username=ADMIN, nick=ADMIN, password=powerjob_admin, webHook=null, phone=null, email=null, extra=null)
   ```
2. 打开web[管理页面](http://192.168.31.201:7700/)
3. 使用初始密码登录

## 应用初始化
1. 登录页面
2. 在`应用管理`点击新增按钮
3. `appName`填写同项目名
4. 创建Spring项目
5. 更改配置
   ```yaml
   powerjob:
      worker:
         app-name: ${appName}
         server-address: ${服务器地址}
         protocol: http
         # 受PowerJob下发任务控制端口，因为是同台机器测试，故采用随机端口
         akka-port: ${random.int(1025,65535)}
   ```
6. 完成项目内容
7. 页面进入对应应用
8. 在`任务管理`页面新建任务
9. 保存

# 执行类型
## 单机
[BasicScheduler.java](src/main/java/com/example/BasicScheduler.java)
> 选择一台机器执行

## 广播
[BroadcastScheduler.java](src/main/java/com/example/BroadcastScheduler.java)
> `preProcess``postProcess`选择一台机器执行, `process`所有机器执行。
> `postProcess`参数为`preProcess`结果及所有`process`结果。

## MapReduce
[MapReduceSchedule.java](src/main/java/com/example/MapReduceSchedule.java)
> 选择一台机器执行`process`。首个任务调用`isRootTask`返回`true`。
> 
> `process`可调用`map`方法分发任务。分发的任务将每个独立选择一台机器执行`process`(可继续分发)。
> 分发的任务使用`context.getSubTask`获取参数, 使用`context.getTaskName()`获取任务名称(子任务由`map`方法指定, 根任务固定为`TaskConstant.ROOT_TASK_NAME`)。
> 
> 当所有任务执行完成后，选择一台机器调用`reduce`, 参数为所有`process`结果。

