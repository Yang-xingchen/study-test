# mybatis
[文档](https://mybatis.org/mybatis-3/zh_CN/index.html)

## 使用方式
### java注解
> 可使用java注解定义mapper，仅能提供简易的功能

基本使用: [annotation](src/main/java/com/example/annotation)

### mapper.xml
> 可使用xml定义mapper，提供完整的功能

基本使用: [base](src/main/java/com/example/base)
[文档](https://mybatis.org/mybatis-3/zh_CN/sqlmap-xml.html)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="...">

</mapper>
```

NOTE:
1. xml与java接口目录层级需一样，但是xml根目录为`resources`, java接口根目录为`java`
2. namespace(命名空间)为java接口引用，必须指定, 这样可以进行接口绑定

## 写
insert、update、delete进行写操作。

其中返回内容为变更行数。

### 自动生成字段(自增主键等)
useGeneratedKeys: （仅适用于insert和update）这会令MyBatis使用JDBC的getGeneratedKeys方法来取出由数据库内部生成的主键（比如：像MySQL和SQL Server这样的关系型数据库管理系统的自动递增字段），默认值：false。
keyProperty: （仅适用于insert和update）指定能够唯一识别对象的属性，MyBatis会使用getGeneratedKeys的返回值或insert语句的selectKey子元素设置它的值，默认值：未设置（unset）。如果生成列不止一个，可以用逗号分隔多个属性名称。
keyColumn: （仅适用于insert和update）设置生成键值在表中的列名，在某些数据库（像PostgreSQL）中，当主键列不是表中的第一列的时候，是必须设置的。如果生成列不止一个，可以用逗号分隔多个属性名称。

使用`@Options`定义属性，内容同mapper.xml

## 读
select进行读操作。

其中返回内容为读取内容。