package com.example.ai;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class AiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	/**
	 * 阻塞式访问，在获得完整回答后返回
	 */
	@Bean
	public CommandLineRunner call() {
		return args -> {
			OllamaChatModel model = OllamaChatModel.builder()
					.ollamaApi(new OllamaApi())
					.build();
			ChatOptions options = ChatOptions.builder()
					.model("deepseek-r1:32b")
					.build();
			ChatResponse response = model.call(new Prompt("你是谁?", options));
			System.out.println(response.getResult().getOutput().getText());
		};
	}

	/**
	 * 流式访问，每个token返回
	 */
	@Bean
	public CommandLineRunner stream() {
		return args -> {
			OllamaChatModel model = OllamaChatModel.builder()
					.ollamaApi(new OllamaApi())
					.build();
			CountDownLatch latch = new CountDownLatch(1);
			ChatOptions options = ChatOptions.builder()
					.model("deepseek-r1:32b")
					.build();
			model.stream(new Prompt("你是谁?", options))
					.doFinally(signalType -> latch.countDown())
					.subscribe(chatResponse -> System.out.print(chatResponse.getResult().getOutput().getText()));
			latch.await();
		};
	}

}
