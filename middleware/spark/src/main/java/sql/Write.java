package sql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Write {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        Dataset<Row> dataFrame = spark.createDataFrame(getData(), Order.class);
        dataFrame.write().json("output");
        spark.close();
    }

    @NotNull
    private static List<Order> getData() {
        List<Order> orderList = new ArrayList<>(1000);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            orderList.add(new Order(Integer.toString(i),
                    Math.abs(random.nextLong() % 10),
                    Math.abs(random.nextLong() % 100),
                    Math.abs(random.nextLong() % 10)));
        }
        return orderList;
    }

}
