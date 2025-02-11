[base](src/main/java/base)
- WorkCount: [WordCount.java](src/main/java/base/WordCount.java)
- 读: [Read.java](src/main/java/base/Read.java)
- 写: [Write.java](src/main/java/base/Write.java)

# Job/Stage/Task
- Job: 作业，完整的执行流程。即count(Job)=count(行动算子)。
- Stage: 阶段，每次读写算做一个阶段。即count(Stage)=count(Shuffle)+1。各阶段串行执行。
- Task: 任务，每个阶段最后RDD分区数量总和。

Job 1=(1..n) Stage 1=(1..n) Task

# Partition
分区。将数据分成多个区，各区数据不重复。
k-v类型相同key放在同一个区。
Shuffle操作会重新分区。

- 自定义分区结果: [Partition.java](src/main/java/base/Partition.java)
- 自定义分区数量: [StagePartition.java](src/main/java/base/StagePartition.java)

# Shuffle
将分区内数据重新打乱分发操作称为Shuffle。Shuffle操作是主要的资源消耗原因。
- 可改变分区数目。
- Shuffle一定会落盘。
- 需等待所有数据Shuffle操作执行完毕后才继续执行后续操作。

# RDD
Resilient Distributed Datasets弹性分布式数据集。

- 封装单个简单的数据处理，复杂处理需多个RDD组合
- 不保存具体数据值
- 可处理多个分区

## 数据处理分类
- value: 每条数据只有值，处理也是按值处理。[BaseRdd.java](src/main/java/base/BaseRdd.java)
- key-value: 每条数据包含键值对，可按键值处理。[PairRdd.java](src/main/java/base/PairRdd.java)

## 算子(方法处理分类)
指RDD内的方法。对数据进行操作。
- 转换算子: 对数据进行转化操作，返回结果还是RDD。 一般不会创建执行任务。
- 行动算子: 对数据进行收集操作，返回结果不是RDD。 会创建并执行任务。

# 依赖
相邻RDD间数据分区的关系。下游RDD依赖直接上游RDD。

## 窄依赖
- 上游RDD数据被一个RDD独享。
- 上游 (1..n)=1 下游。
- 不会执行`shuffle`操作。

## 宽依赖
- 上游RDD数据被多个RDD共享。
- 上游 (1..n)=n 下游。
- 会执行`shuffle`操作。

# 持久化
避免重复计算，将计算中间结果保存。
- cache: 缓存。不切断血缘关系，存储在内存。[CacheRdd.java](src/main/java/base/CacheRdd.java)
- checkPoint: 检测点。切断血缘关系，通常存储在HDFS。会重新计算。[CheckPointRdd.java](src/main/java/base/CheckPointRdd.java)

# Broadcast
广播变量，当task使用到共享变量时，避免多次传输时使用。
该方法可将共享变量按工作节点分发。(默认采用task分发)
