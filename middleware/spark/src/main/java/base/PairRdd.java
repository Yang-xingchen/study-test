package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PairRdd {

    public static final List<Tuple2<String, Integer>> DATA = Stream
            .of(
                    new Tuple2<>("ABC", 1),
                    new Tuple2<>("abc", 2),
                    new Tuple2<>("Abc", 3),
                    new Tuple2<>("Abc", 3),
                    new Tuple2<>("Abc", 4),
                    new Tuple2<>("ABc", 3),
                    new Tuple2<>("aBc", 9)
            )
            .collect(Collectors.toList());

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        List<Tuple2<String, Integer>> res = javaSparkContext
                .parallelizePairs(DATA)
                // 过滤: 移除 ("aBc", 9)
                .filter(t -> t._2() < 5)
                // 去重: K-V相同才去重
                .distinct()
                // 映射值: 双倍
                .mapValues(i -> i * 2)
                // 排序: 按key排
                .sortByKey()
                .collect();
        res.forEach(System.out::println);
        spark.close();
    }

}
