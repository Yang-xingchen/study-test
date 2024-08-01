package sql;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Schema {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();

        // 定义schema
        StructType schema = DataTypes.createStructType(Stream.of(
                DataTypes.createStructField("id", DataTypes.StringType, false),
                DataTypes.createStructField("name", DataTypes.StringType, false),
                DataTypes.createStructField("age", DataTypes.IntegerType, false)
        ).collect(Collectors.toList()));

        // 获取数据
        List<String> data = Stream.of(
                "001 user1 10",
                "002 user2 20",
                "003 user3 15",
                "004 user4 35"
        ).collect(Collectors.toList());
        JavaRDD<Row> dataRdd = new JavaSparkContext(spark.sparkContext())
                .parallelize(data)
                .map(str -> {
                    String[] s = str.split(" ");
                    return RowFactory.create(s[0], s[1], Integer.parseInt(s[2]));
                });

        /*
         * +---+-----+---+
         * | id| name|age|
         * +---+-----+---+
         * |001|user1| 10|
         * |002|user2| 20|
         * |003|user3| 15|
         * |004|user4| 35|
         * +---+-----+---+
         */
        spark.createDataFrame(dataRdd, schema).show();

        spark.close();
    }

}
