package com.example.seata.provider;

import org.apache.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private TestMapper testMapper;

    @GetMapping("/create")
    public Long create(@RequestParam("init") Integer init) {
        log.info("XID: {}", RootContext.getXID());
        Entry entry = new Entry();
        entry.setVal(init);
        testMapper.save(entry);
        return entry.getId();
    }

    @GetMapping("/add")
    public void add(@RequestParam("id") Long id, @RequestParam("i") Integer i, @RequestParam("err") Boolean err) {
        log.info("ID: {}, XID: {}", id, RootContext.getXID());
        if (err) {
            throw new RuntimeException("err");
        }
        testMapper.add(id, i);
    }

    @GetMapping("/get")
    public Integer get(@RequestParam("id") Long id) {
        log.info("ID: {}, XID: {}", id, RootContext.getXID());
        return testMapper.get(id);
    }

}
