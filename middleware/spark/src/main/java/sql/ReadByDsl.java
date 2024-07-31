package sql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.sum;

public class ReadByDsl {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        // 需先通过sql.Write生成数据
        Dataset<Row> dataset = spark.read().json("output");
        dataset.select(col("userId"), col("count"))
                .groupBy(col("userId"))
                .agg(sum("count"))
                .show();
        spark.close();
    }

}
