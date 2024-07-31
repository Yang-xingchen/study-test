package sql;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Aggregator;
import org.apache.spark.sql.functions;
import scala.Tuple2;

public class UdafSingle {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        // 需先通过sql.Write生成数据
        spark.read().json("output").createOrReplaceTempView("order");
        spark.udf().register("a", functions.udaf(new UdafAggregator(), Encoders.LONG()));
        spark.sql("select goodId, a(count) as avg from order group by goodId").show();
        spark.close();
    }

    public static class UdafAggregator extends Aggregator<Long, Tuple2<Long, Long>, Double> {

        @Override
        public Tuple2<Long, Long> zero() {
            return new Tuple2<>(0L, 0L);
        }

        @Override
        public Tuple2<Long, Long> reduce(Tuple2<Long, Long> b, Long a) {
            return new Tuple2<>(b._1() + a, b._2() + 1);
        }

        @Override
        public Tuple2<Long, Long> merge(Tuple2<Long, Long> b1, Tuple2<Long, Long> b2) {
            return new Tuple2<>(b1._1() + b2._1(), b1._2() + b2._2());
        }

        @Override
        public Double finish(Tuple2<Long, Long> reduction) {
            return 1.0 * reduction._1() / reduction._2();
        }

        @Override
        public Encoder<Tuple2<Long, Long>> bufferEncoder() {
            return Encoders.tuple(Encoders.LONG(), Encoders.LONG());
        }

        @Override
        public Encoder<Double> outputEncoder() {
            return Encoders.DOUBLE();
        }

    }

}
