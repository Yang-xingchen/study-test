package base;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCount {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = env.readTextFile("flink/base/WordCount.txt");
        streamSource
                .flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
                    for (String s : line.split(" ")) {
                        out.collect(Tuple2.of(s.toLowerCase(), 1L));
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.LONG))
                .name("flat")
                .keyBy(tuple -> tuple.f0)
                .sum(1)
                .name("count")
                .print();
        env.execute();
    }

}
