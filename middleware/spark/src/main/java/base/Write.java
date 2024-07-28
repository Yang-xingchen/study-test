package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Write {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        javaSparkContext.parallelize(Stream.of(1, 2, 3, 4).collect(Collectors.toList()))
                .saveAsTextFile("./output");
        spark.stop();
    }

}
