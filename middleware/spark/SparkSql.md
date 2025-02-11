[sql](src/main/java/sql)
- schema: [Schema.java](src/main/java/sql/Schema.java)
- 写入: [Write.java](src/main/java/sql/Write.java)

# 操作方式
- SQL: 使用sql语法操作[ReadBySql.java](src/main/java/sql/ReadBySql.java)
- DSL: 使用编程方式操作[ReadByDsl.java](src/main/java/sql/ReadByDsl.java)

# 自定义函数对象
- UDF: 用户自定义函数，类似map操作[Udf.java](src/main/java/sql/Udf.java)
- UDAF: 用户自定义聚合函数，类似reduce操作[单参数](src/main/java/sql/UdafSingle.java) [多参数](src/main/java/sql/UdafMulti.java)
