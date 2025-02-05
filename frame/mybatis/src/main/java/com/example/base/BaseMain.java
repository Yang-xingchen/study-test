package com.example.base;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class BaseMain implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BaseMain.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(String... args) throws Exception {
        Long uid = save();
        find(uid);
        update(uid);
        delete(uid);
    }

    private Long save() {
        User user = new User();
        user.setUname("user");
        Assertions.assertEquals(1, userMapper.save(user));
        log.info("save: uid: {}", user.getUid());
        Assertions.assertNotNull(user.getUid());
        return user.getUid();
    }

    private void find(Long uid) {
        User user = userMapper.find(uid);
        log.info("find: {}", user);
        Assertions.assertNotNull(user);
    }

    private void update(Long uid) {
        Assertions.assertEquals(1, userMapper.update(uid, "user2"));
        Assertions.assertEquals("user2", userMapper.find(uid).getUname());
    }

    private void delete(Long uid) {
        Assertions.assertEquals(1, userMapper.delete(uid));
        Assertions.assertNull(userMapper.find(uid));
    }

}
