package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseRdd {

    public static final List<String> DATA = Stream
            .of("Abc", "Abcdef", "bC", "Dd","eC", "dD")
            .collect(Collectors.toList());

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        List<String> res = javaSparkContext
                .parallelize(DATA)
                // 过滤: 移除 Abcdef
                .filter(s -> s.length() < 5)
                // 映射: 转大写
                .map(String::toUpperCase)
                // 去重: 合并 Dd dD
                .distinct()
                // 展开: 按字符串长度复制多份
                .flatMap(s -> {
                    List<String> list = new ArrayList<>(s.length());
                    for (int i = 0; i < s.length(); i++) {
                        list.add(s);
                    }
                    return list.iterator();
                })
                // 排序: 字典序
                .sortBy(s -> s, true, 2)
                .collect();
        res.forEach(System.out::println);
        spark.close();
    }

}
