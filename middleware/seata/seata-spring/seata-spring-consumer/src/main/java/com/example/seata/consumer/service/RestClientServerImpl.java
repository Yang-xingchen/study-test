package com.example.seata.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import static com.example.seata.consumer.SeataSpringConsumerApplication.PROVIDER_NAME;

public class RestClientServerImpl implements Server {

    @Autowired
    private RestClient restClient;

    @Override
    public Long create(int init) {
        return restClient.get().uri("http://" + PROVIDER_NAME + "/create?init=" + init).retrieve().body(Long.class);
    }

    @Override
    public void add(Long id, int i, boolean err) {
        restClient.get().uri("http://" + PROVIDER_NAME + "/add?id=" + id + "&i=" + i + "&err=" + err).retrieve().body(String.class);
    }

    @Override
    public Integer get(Long id) {
        return restClient.get().uri("http://" + PROVIDER_NAME + "/get?id=" + id, Integer.class).retrieve().body(Integer.class);
    }

}
