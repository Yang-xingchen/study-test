package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StagePartition {

    public static final List<Integer> DATA = Stream
            .of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
            .collect(Collectors.toList());

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local[12]")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        javaSparkContext
                // stage1: 2
                .parallelize(DATA, 2)
                .map(i -> {
                    // 2 thread
                    System.out.println("parallelize[" + Thread.currentThread().hashCode() + "]: " + i);
                    return i;
                })
                // stage2: 6
                .repartition(12)
                .map(i1 -> {
                    // 6 thread
                    System.out.println("repartition[" + Thread.currentThread().hashCode() + "]: " + i1);
                    return i1;
                })
                .coalesce(6)
                .map(i11 -> {
                    // 6 thread
                    System.out.println("coalesce1[" + Thread.currentThread().hashCode() + "]: " + i11);
                    return i11;
                })
                // stage3: 3
                .coalesce(3, true)
                .map(i11 -> {
                    // 3 thread
                    System.out.println("coalesce2[" + Thread.currentThread().hashCode() + "]: " + i11);
                    return i11;
                })
                .collect()
                .forEach(System.out::println);
        System.out.println("open: http://localhost:4040");
        TimeUnit.HOURS.sleep(1);
        spark.close();
    }

}
