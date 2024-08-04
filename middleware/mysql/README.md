# 安装

1. 下载: https://dev.mysql.com/downloads/mysql/
2. 解压: ```tar -zxvf mysql-8.4.2-1.el7.x86_64.rpm-bundle.tar```
3. 安装:
    ```
   rpm -ivh mysql-community-common-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-libs-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-libs-compat-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-embedded-compat-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-devel-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-server-8.4.2-1.el7.x86_64.rpm
   rpm -ivh mysql-community-client-8.4.2-1.el7.x86_64.rpm
   ```
4. 初始化: ```mysqld --initialize```
5. 调整数据文件所属: ```chown mysql:mysql /var/lib/mysql -R```
6. 启动: ```systemctl start mysqld```
7. 设置自启: ```systemctl enable mysqld```
8. 查看初始密码: ```cat /var/log/mysqld.log | grep password```
9. 设置密码: ```alter user user() identifed by '123456';```
10. 允许所有连接:
   ```
   use mysql;
   update user set host='%' where user='root';
   ```
11. 刷新: ```flush privileges;```

# 其他
- 打开mysql客户端命令: ```mysql -uroot -p```
- 配置文件: ```/var/lib/mysql```
- 日志文件: ```/var/log/mysqld.log```
