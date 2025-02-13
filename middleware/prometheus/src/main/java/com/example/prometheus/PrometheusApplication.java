package com.example.prometheus;

import io.micrometer.core.instrument.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@SpringBootApplication
public class PrometheusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrometheusApplication.class, args);
	}

	@Bean
	public CommandLineRunner test(MeterRegistry registry) {
		return args -> {
			long start = System.currentTimeMillis();
			// 计数，只增不减
			Counter counter = Counter.builder("spring.test.counter")
					.tag("tag", "counter")
					.register(registry);

			// 记录运行时间
			Timer timer = Timer.builder("spring.test.timer")
					.tag("tag", "timer")
					.register(registry);


			// 分布统计
			DistributionSummary summary = DistributionSummary.builder("spring.test.summary")
					.publishPercentiles(0.5, 0.9, 0.99)
					.tag("tag", "summary")
					.register(registry);
			Random random = new Random();

			// 数值，可增可减
			Gauge gauge = Gauge.builder("spring.test.gauge", () -> {
						counter.increment();
						timer.record(() -> {
							for (int i = 1; i < 30; i++) {
								Math.sin(i / 10.0);
								summary.record(random.nextInt(500));
							}
						});
						System.out.println("gauge get: " + LocalTime.now());
						return System.currentTimeMillis() - start;
					})
					.tag("tag", "gauge")
					.register(registry);
			System.out.println("registry");
		};
	}

}
