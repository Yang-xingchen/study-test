# java.compiler
1. 运行期间动态编译java代码到文件
2. 编译期注解处理

## 动态编译
- 代码写在文件中: [CompileByFile.java](CompileByFile.java)
- 代码写在内存中(String): [CompileByMemory.java](CompileByMemory.java)

## 编译期注解
- 打印解析代码内容: [print](processor/print)
- 生成代码: [write](processor/write)
- ~~读取内容: [toPath](processor/toPath)~~

**NOTE: 生成代码无法改变源文件，只允许创建新文件**