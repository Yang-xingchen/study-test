package com.example.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@SpringBootApplication
public class HadoopApplication {

	public static void main(String[] args) {
		SpringApplication.run(HadoopApplication.class, args);
	}

	private CommandLineRunner wrapper(Consumer<FileSystem> consumer) {
		return args -> {
			Configuration conf = new Configuration();
			try (FileSystem fileSystem = FileSystem.get(URI.create("hdfs://192.168.31.201:8020"), conf, "root")) {
				consumer.accept(fileSystem);
			}
		};
	}

	@Bean
	public CommandLineRunner writeAndRead() {
		return wrapper(fileSystem -> {
			try {
				fileSystem.mkdirs(new Path("/test"));
				Path path = new Path("/test/a.txt");
				// 创建文件
				FSDataOutputStream outputStream;
				if (!fileSystem.exists(path)) {
					outputStream = fileSystem.create(path);
				} else {
					outputStream = fileSystem.append(path);
				}
				// 写
				try {
					outputStream.write("txt1\n".getBytes(StandardCharsets.UTF_8));
					outputStream.write("txt2\n".getBytes(StandardCharsets.UTF_8));
				} finally {
					outputStream.close();
				}
				// 读
				try (FSDataInputStream inputStream = fileSystem.open(path)) {
					byte[] bytes = inputStream.readAllBytes();
					System.out.println(new String(bytes));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
