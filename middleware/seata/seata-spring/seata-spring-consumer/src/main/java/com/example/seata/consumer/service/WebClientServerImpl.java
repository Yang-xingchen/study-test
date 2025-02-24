package com.example.seata.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.seata.consumer.SeataSpringConsumerApplication.PROVIDER_NAME;

public class WebClientServerImpl implements Server {

    @Autowired
    private WebClient webClient;

    @Override
    public Long create(int init) {
        return webClient.get().uri("http://" + PROVIDER_NAME + "/create?init=" + init).retrieve().bodyToMono(Long.class).block();
    }

    @Override
    public void add(Long id, int i, boolean err) {
        webClient.get().uri("http://" + PROVIDER_NAME + "/add?id=" + id + "&i=" + i + "&err=" + err).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public Integer get(Long id) {
        return webClient.get().uri("http://" + PROVIDER_NAME + "/get?id=" + id, Integer.class).retrieve().bodyToMono(Integer.class).block();
    }

}
