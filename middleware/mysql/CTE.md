# CTE
Common Table Expressions(公用表表达式)，允许用户定义一个临时的结果集可在同一个查询中多次引用。

语法
```
WITH  [RECURSIVE]
         cte_name [(col_name [,  col_name] ...)] AS (subquery)
         [, cte_name [(col_name [,  col_name] ...)] AS (subquery)] ...
 ```

## WITH
使用`WITH`定义CTE，可在查询中按照表的方式使用，支持单个SQL多次使用。

> 查询订单总金额及商品总数

SQL
```
WITH item AS (
	SELECT oi.*, g.g_name as 'good_name', g.g_type as 'type', t.t_name as 'type_name', g.g_money as 'price', oi.oi_count * g.g_money as 'money'
	FROM t_order_item oi 
	INNER JOIN t_good g on oi.oi_gid=g.g_id
	INNER JOIN t_type t on g.g_type=t.t_id
)
SELECT o.*, SUM(i.money) as money, SUM(i.oi_count) as count
FROM t_order o INNER JOIN item i on o.o_id=i.oi_oid
GROUP BY o.o_id
```
结果
```
o_id|o_status|o_create_time      |o_uid|money  |count|
----+--------+-------------------+-----+-------+-----+
   1|       0|2024-08-01 12:00:00|    1| 351900|    9|
   2|       1|2024-08-01 12:00:00|    1| 100000|    1|
   3|       1|2024-08-03 11:00:00|    2|   2100|    7|
   4|       1|2024-08-05 15:00:00|    2| 500600|    3|
   5|       2|2024-08-05 10:00:00|    1|    600|    2|
   6|       2|2024-08-07 14:00:00|    3|1000000|    2|
```

## RECURSIVE
CTE支持递归，使用`RECURSIVE`可在表达式内使用所定义的表达式。

> 递归查询类型

SQL
```
WITH RECURSIVE r AS (
	SELECT root.t_id, root.t_name, root.t_parent, root.t_level, root.t_id as root, CONCAT('', root.t_id) as t_path
	FROM t_type root
	WHERE t_parent IS NULL
	UNION ALL
	SELECT c.t_id, c.t_name, c.t_parent, c.t_level, r.root as root, CONCAT(r.t_path, ',', c.t_id)
	FROM t_type c
	INNER JOIN r ON c.t_parent=r.t_id
)
SELECT t_id, t_name, t_parent, t_level, t_path
FROM r
ORDER BY root, t_level
```
结果
```
t_id|t_name|t_parent|t_level|t_path|
----+------+--------+-------+------+
   1|日用品   |        |      0|1     |
   2|纸巾    |       1|      1|1,2   |
   3|笔     |       1|      1|1,3   |
   4|电子产品  |        |      0|4     |
   5|手机    |       4|      1|4,5   |
   6|电脑    |       4|      1|4,6   |
```

