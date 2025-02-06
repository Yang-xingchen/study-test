package com.example.result;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultMain implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ResultMain.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(String... args) throws Exception {
        Long uid = init();
        findUser(uid);
    }

    private Long init() {
        Role role = new Role();
        role.setName("role");
        userMapper.saveRole(role);

        User user = new User();
        user.setName("user");
        user.setRole(role);
        userMapper.saveUser(user);

        List<Long> pids = Stream.of("p1", "p2", "p3")
                .map(name -> {
                    Permissions permissions = new Permissions();
                    permissions.setName(name);
                    return permissions;
                })
                .peek(userMapper::savePermissions)
                .map(Permissions::getId)
                .toList();

        userMapper.saveRolePermissions(role.getId(), pids);
        return user.getId();
    }

    private void findUser(Long uid) {
        User user = userMapper.findUser(uid);
        log.info("user: {}", user);
        Assertions.assertEquals("user", user.getName());
        Assertions.assertInstanceOf(User.Man.class, user);

        Role role = user.getRole();
        Assertions.assertNotNull(role);
        Assertions.assertEquals("role", role.getName());

        Set<String> pNames = role.getPermissions().stream().map(Permissions::getName).collect(Collectors.toSet());
        Assertions.assertEquals(Set.of("p1", "p2", "p3"), pNames);
    }

}
