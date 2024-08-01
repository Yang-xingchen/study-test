package streaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategy;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ByKafka {

    public static void main(String[] args) throws InterruptedException {
        SparkConf sparkConf = new SparkConf()
                .setMaster("local")
                .setAppName("test");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));

        // kafka配置
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.31.201:9092,192.168.31.202:9092,192.168.31.203:9092");
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "spark");
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 数据处理
        // 数据发送于: middleware/kafka/src/main/java/com/example/kafka/KafkaApplication.java
        ConsumerStrategy<Object, Object> consumerStrategy = ConsumerStrategies.Subscribe(Collections.singleton("random"), kafkaParams);
        JavaInputDStream<ConsumerRecord<Object, Object>> dStream = KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(), consumerStrategy);
        dStream
                .map(ConsumerRecord::value)
                .print();

        // 启动
        jssc.start();
        jssc.awaitTermination();
    }

}
