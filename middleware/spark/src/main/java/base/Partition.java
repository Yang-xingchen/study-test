package base;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Partition {

    public static final List<Integer> DATA = Stream
            .of(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .collect(Collectors.toList());

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());
        javaSparkContext
                .parallelize(DATA)
                .groupBy(i -> i)
                .groupByKey(new org.apache.spark.Partitioner() {
                    @Override
                    public int numPartitions() {
                        return 3;
                    }

                    @Override
                    public int getPartition(Object key) {
                        return key.hashCode() % 3;
                    }
                })
                .map(Tuple2::_1)
                .saveAsTextFile("output");
        spark.close();
    }

}
