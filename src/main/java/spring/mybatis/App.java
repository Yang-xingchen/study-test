package spring.mybatis;


import lombok.extern.java.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import spring.mybatis.mapper.UserMapper;
import spring.mybatis.model.Order;
import spring.mybatis.model.User;

import java.util.stream.Stream;

@PropertySource("classpath:/mybatis/spring-mybatis.properties")
@MapperScan("spring.mybatis.mapper")
@SpringBootApplication
@Log
public class App implements ApplicationRunner {

    private final UserMapper userMapper;

    @Autowired
    public App(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        addUserTest();
//        selectUserTest();
        selectUserOrderTest();
    }

    void addUserTest(){
        Stream.of("user1", "user2", "user3").forEach(s -> userMapper.save(User.builder().name(s).build()));
    }

    void selectUserTest() {
        log.info(userMapper.findById(1).toString());
        userMapper.findAll().stream().map(User::toString).forEach(log::info);
    }

    void selectUserOrderTest(){
        userMapper.findAll()
                .forEach(user -> user.getOrders().stream().map(Order::toString).forEach(log::info));
    }

}
