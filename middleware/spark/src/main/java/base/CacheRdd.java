package base;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.Collections;

public class CacheRdd {

    /**
     * map + plus  + zip
     *     | multi |
     */
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local[2]")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());

//        JavaRDD<Integer> map = javaSparkContext.parallelize(Collections.singletonList(1))
//                .map(i -> {
//                    // 执行2次
//                    System.out.println("1. [" + Thread.currentThread() + "]: " + i);
//                    return i;
//                });
        JavaRDD<Integer> map = javaSparkContext.parallelize(Collections.singletonList(1))
                .map(i -> {
                    // 执行1次
                    System.out.println("1. [" + Thread.currentThread() + "]: " + i);
                    return i;
                })
                .cache();

        JavaRDD<Integer> plus = map.map(i -> {
            // 执行1次
            System.out.println("2.1 [" + Thread.currentThread() + "]: " + i);
            return i + 1;
        });
        JavaRDD<Integer> multi = map.map(i -> {
            // 执行1次
            System.out.println("2.2 [" + Thread.currentThread() + "]: " + i);
            return i * 2;
        });
        JavaRDD<Integer> zip = plus.zip(multi).map(tuple -> {
            // 执行1次
            System.out.println("3. [" + Thread.currentThread() + "]: " + (tuple._1() + tuple._2()));
            return tuple._1() + tuple._2();
        });
        zip.collect().forEach(System.out::println);
        spark.close();
    }

}
