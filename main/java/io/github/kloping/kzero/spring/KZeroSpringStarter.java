package io.github.kzero.spring;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author github.kloping
 */
@SpringBootApplication(scanBasePackages = {"io.github.kzero.spring"})
@MapperScan("io.github.kzero.spring")
@Configuration
public class KZeroSpringStarter {

    public static ConfigurableApplicationContext run(String id) {
        SpringApplication application = new SpringApplication(new Class[]{KZeroSpringStarter.class});
        ConfigurableApplicationContext configuration = application.run(new String[]{"--id=" + id});
        return configuration;
    }


    @Autowired
    private Environment env;

    @Value("${id}")
    private String id;

    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty(String.format("bot-%s.spring.datasource.url", id)));
        dataSource.setUsername(env.getProperty(String.format("bot-%s.spring.datasource.username", id)));
        dataSource.setPassword(env.getProperty(String.format("bot-%s.spring.datasource.password", id)));
        return dataSource;
    }
}
