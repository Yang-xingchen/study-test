package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCount {

    public static final List<String> DATA = Stream
            .of("Abc", "Abcdef", "bC", "Dd","eC", "dD", "Abcdef", "bC", "bC", "Dd","eC", "dD", "Abcdef", "bC")
            .collect(Collectors.toList());

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        Map<String, Integer> res = javaSparkContext
                .parallelize(DATA)
                .groupBy(s -> s)
                .mapValues(strings -> {
                    int count = 0;
                    for (String s : strings) {
                        count++;
                    }
                    return count;
                })
                .collectAsMap();
        res.forEach((k, v) -> System.out.println(k + "\t" + v));
        spark.close();
    }

}
