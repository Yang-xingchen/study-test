package base;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckPointRdd {

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
//        // 需提前创建目录
        javaSparkContext.setCheckpointDir("checkpoint");
        JavaRDD<Integer> cache = javaSparkContext
                .parallelize(DATA)
                .map(i -> {
                    System.out.println("map: " + i);
                    return i;
                })
                .cache();
        cache.checkpoint();
        cache.collect().forEach(System.out::println);

        // 获取checkpoint输出文件
        Optional<String> checkpointFile = cache.getCheckpointFile();
        System.out.println("file:" + checkpointFile.orElse(null));

        javaSparkContext = new JavaSparkContext(spark.sparkContext());
        // 读取checkpoint
        javaSparkContext.checkpointFile(checkpointFile.get())
                .collect()
                .forEach(System.out::println);

        // job: 3
        System.out.println("open: http://localhost:4040");
        TimeUnit.HOURS.sleep(1);
        spark.close();
    }

}
