package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Accumulators {

    public static final List<Integer> DATA = Stream
            .of(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .collect(Collectors.toList());

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        LongAccumulator longAccumulator = spark.sparkContext().longAccumulator("longAccumulator");
        javaSparkContext
                .parallelize(DATA)
                .map(i -> {
                    longAccumulator.add(i);
                    return i;
                })
                .collect()
                .forEach(System.out::println);
        System.out.println("sum: " + longAccumulator.sum());

        // job: 3
        System.out.println("open: http://localhost:4040");
        TimeUnit.HOURS.sleep(1);
        spark.close();
    }

}
