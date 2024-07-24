package com.example.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class KafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

	@Bean
	public NewTopic testTopic() {
		return TopicBuilder
				.name("test")
				.partitions(10)
				.replicas(1)
				.build();
	}

	@Bean
	public CommandLineRunner test(KafkaTemplate<String, byte[]> kafkaTemplate) {
		return args -> {
			kafkaTemplate.send("test", "test1".getBytes(StandardCharsets.UTF_8));
		};
	}

	@KafkaListener(id = "test", topics = "test")
	public void listener(String data) {
		System.out.println("listener: " + data);
	}

}
