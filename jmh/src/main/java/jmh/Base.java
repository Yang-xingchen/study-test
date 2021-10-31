package jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@Warmup(iterations = 5, batchSize = 3, time = 1)
@Measurement(iterations = 5, batchSize = 1, time = 1)
@Timeout(time = 3)
//@Threads(1)
@Fork(1)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Base {

    @Param({"100", "10000", "1000000"})
    private int size;

    private List<Double> data;

    @Setup(Level.Invocation)
    public void setUp() {
        Random random = new Random();
        data = DoubleStream.generate(random::nextDouble).limit(size).boxed().collect(Collectors.toList());
    }

    @Benchmark
    public void sort(Blackhole blackhole) {
        List<Double> collect = data.stream().sorted().collect(Collectors.toList());
        blackhole.consume(collect);
    }

    @Benchmark
    public void parallelSort(Blackhole blackhole) {
        List<Double> collect = data.stream().parallel().sorted().collect(Collectors.toList());
        blackhole.consume(collect);
    }

    public static void main(String[] args) throws Throwable {
        Options options = new OptionsBuilder()
                .include(Base.class.getSimpleName())
                .jvmArgs("-ea")
                .build();
        new Runner(options).run();
    }

}
