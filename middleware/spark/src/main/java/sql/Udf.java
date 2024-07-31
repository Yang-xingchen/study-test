package sql;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;

public class Udf {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        // 需先通过sql.Write生成数据
        spark.read().json("output").createOrReplaceTempView("order");
        spark.udf().register("prefix", (UDF2<String, Long, String>) (pre, column) -> pre + column, DataTypes.StringType);
        spark.sql("select prefix('user', userId) as user, prefix('good', goodId) as good, count from order").show();
        spark.close();
    }

}
