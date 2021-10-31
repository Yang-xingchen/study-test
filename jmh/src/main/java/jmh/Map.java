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
@Warmup(iterations = 5, batchSize = 8, time = 1)
@Measurement(iterations = 5, batchSize = 1, time = 1)
@Timeout(time = 3)
//@Threads(1)
@Fork(1)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Map {

    @Param({"1", "10000", "100000000"})
    private int size;

    private List<Double> data;

    @Setup(Level.Invocation)
    public void setUp() {
        Random random = new Random();
        data = DoubleStream.generate(random::nextDouble).limit(size).boxed().collect(Collectors.toList());
    }

    @Benchmark
    public void sin(Blackhole blackhole) {
        data.stream().map(Math::sin).map(Math::tanh).forEach(blackhole::consume);
    }

    @Benchmark
    public void parallelSin(Blackhole blackhole) {
        data.stream().parallel().map(Math::sin).map(Math::tanh).forEach(blackhole::consume);
    }

    public static void main(String[] args) throws Throwable {
        Options options = new OptionsBuilder()
                .include(Map.class.getSimpleName())
                .jvmArgs("-ea")
                .build();
        new Runner(options).run();
    }

}
