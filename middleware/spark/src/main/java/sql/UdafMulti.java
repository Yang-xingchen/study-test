package sql;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Aggregator;
import org.apache.spark.sql.functions;
import scala.Serializable;

import java.util.HashMap;
import java.util.Map;

public class UdafMulti {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("test")
                .master("local")
                .getOrCreate();
        // 需先通过sql.Write生成数据
        spark.read().json("output").createOrReplaceTempView("order");
        spark.udf().register("userBuy", functions.udaf(new UserBuy(), Encoders.bean(Order.class)));
        spark.sql("select goodId, userBuy(id, userId, goodId, count) as userBuy from order group by goodId").show();
        spark.close();
    }

    public static class UdafBuffer implements Serializable {
        private Map<Long, Long> map = new HashMap<>();

        public Map<Long, Long> getMap() {
            return map;
        }

        public void setMap(Map<Long, Long> map) {
            this.map = map;
        }
    }

    public static class UserBuy extends Aggregator<Order, UdafBuffer, String> {

        @Override
        public UdafBuffer zero() {
            return new UdafBuffer();
        }

        @Override
        public UdafBuffer reduce(UdafBuffer b, Order a) {
            b.map.compute(a.getUserId(), (user, count) -> {
                if (count == null) {
                    return Long.valueOf(a.getCount());
                } else {
                    return count + a.getCount();
                }
            });
            return b;
        }

        @Override
        public UdafBuffer merge(UdafBuffer b1, UdafBuffer b2) {
            UdafBuffer res = new UdafBuffer();
            b1.map.forEach((k, v) -> res.map.compute(k, (user, count) -> {
                if (count == null) {
                    return Long.valueOf(v);
                } else {
                    return count + v;
                }
            }));
            b2.map.forEach((k, v) -> res.map.compute(k, (user, count) -> {
                if (count == null) {
                    return Long.valueOf(v);
                } else {
                    return count + v;
                }
            }));
            return res;
        }

        @Override
        public String finish(UdafBuffer reduction) {
            return reduction.map.toString();
        }

        @Override
        public Encoder<UdafBuffer> bufferEncoder() {
            return Encoders.bean(UdafBuffer.class);
        }

        @Override
        public Encoder<String> outputEncoder() {
            return Encoders.STRING();
        }

    }

}
