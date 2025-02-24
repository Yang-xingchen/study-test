package com.example.seata.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static com.example.seata.consumer.SeataSpringConsumerApplication.PROVIDER_NAME;

public class RestTemplateServerImpl implements Server {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Long create(int init) {
        return restTemplate.getForObject("http://" + PROVIDER_NAME + "/create?init=" + init, Long.class);
    }

    @Override
    public void add(Long id, int i, boolean err) {
        restTemplate.getForObject("http://" + PROVIDER_NAME + "/add?id=" + id + "&i=" + i + "&err=" + err, String.class);
    }

    @Override
    public Integer get(Long id) {
        return restTemplate.getForObject("http://" + PROVIDER_NAME + "/get?id=" + id, Integer.class);
    }

}
