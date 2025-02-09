package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PowerJobApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(PowerJobApplication.class, args);
        TimeUnit.MINUTES.sleep(10);
    }

}
