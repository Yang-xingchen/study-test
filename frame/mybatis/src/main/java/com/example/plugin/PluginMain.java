package com.example.plugin;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class PluginMain implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PluginMain.class);

    @Autowired
    public UserMapper userMapper;

    @Override
    public void run(String... args) throws Exception {
        Long uid = save();
        find(uid);
    }

    private Long save() {
        User user = new User();
        user.setName("user");
        Assertions.assertEquals(1, userMapper.save(user));
        log.info("save: uid: {}", user.getId());
        Assertions.assertNotNull(user.getId());
        return user.getId();
    }

    private void find(Long uid) {
        User user = userMapper.find(uid);
        log.info("user: {}", user);
    }

}
