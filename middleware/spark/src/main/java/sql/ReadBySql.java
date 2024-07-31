package sql;

import org.apache.spark.sql.SparkSession;

public class ReadBySql {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        // 需先通过sql.Write生成数据
        spark.read().json("output").createOrReplaceTempView("order");
        spark.sql("select userId, sum(count) from order group by userId").show();
        spark.close();
    }

}
