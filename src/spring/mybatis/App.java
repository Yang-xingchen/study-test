package spring.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Setter;
import lombok.extern.java.Log;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import spring.mybatis.mapper.UserMapper;
import spring.mybatis.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

@Configuration
@PropertySource("classpath:/resources/spring-mybatis.properties")
@MapperScan("spring.mybatis.mapper")
@Setter
@Log
public class App {

    static ApplicationContext applicationContext = new AnnotationConfigApplicationContext("spring.mybatis");

    public static void main(String[] args) {
//        new App().addUserTest();
        new App().selectUserTest();
    }

    void addUserTest(){
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        Stream.of("user1", "user2", "user3").forEach(s -> userMapper.save(User.builder().name(s).build()));
    }

    void selectUserTest() {
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        log.info(userMapper.findById(1).toString());
        userMapper.findAll().stream().map(User::toString).forEach(log::info);
    }

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClass"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));

        dataSource.setFilters("stat,log4j");

        dataSource.setMaxActive(20);
        dataSource.setInitialSize(1);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(1);

        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);

        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        dataSource.setAsyncInit(true);
        return dataSource;
    }

    @Bean
    SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource){
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

}
