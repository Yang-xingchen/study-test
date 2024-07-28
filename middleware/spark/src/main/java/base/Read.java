package base;

import org.apache.spark.sql.SparkSession;

import java.util.List;

public class Read {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        List<String> res = spark
                .read()
                .textFile("README.md")
                .javaRDD()
                .collect();
        System.out.println(res);
        spark.stop();
    }

}
