package com.example.pulsar;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.pulsar.listener.Acknowledgement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PulsarApplication {

	private Logger log = LoggerFactory.getLogger(PulsarApplication.class);

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(PulsarApplication.class, args);
		TimeUnit.MINUTES.sleep(10);
	}

	@Bean
	public CommandLineRunner send(PulsarTemplate<String> pulsarTemplate) {
		return args -> {
			for (int i = 0; i < 10; i++) {
				pulsarTemplate.newMessage(Long.toString(System.currentTimeMillis()))
						.withTopic("topic")
						.withMessageCustomizer(messageBuilder -> messageBuilder.key("key"))
						.send();
			}
		};
	}

	@PulsarListener(topics = "topic",
			subscriptionName = "spring-test",
			ackMode = AckMode.MANUAL,
			batch = true)
	public void listener(List<Message<String>> message, Acknowledgement acknowledgement) {
		long time = System.currentTimeMillis();
		log.info("listener: {}", message.stream().map(Message::getValue).toList());
		message.stream()
				.map(Message::getValue)
				.mapToLong(Long::parseLong)
				.map(t -> time - t)
				.forEach(System.out::println);
		List<MessageId> ids = message.stream().map(Message::getMessageId).toList();
		acknowledgement.acknowledge(ids);
	}

}
