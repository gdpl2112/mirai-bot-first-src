package io.github.kloping.kzero.spring;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.kloping.spt.impls.PackageScannerImpl;
import io.github.kloping.spt.interfaces.component.PackageScanner;
import io.github.kloping.kzero.utils.Utils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author github.kloping
 */
@SpringBootApplication(scanBasePackages = {"io.github.kloping.kzero.spring","io.github.kloping.kzero.hwxb"})
@MapperScan("io.github.kloping.kzero.spring")
@Configuration
public class KZeroSpringStarter {

    public static ConfigurableApplicationContext run(String id) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(KZeroSpringStarter.class);
        SpringApplication application = builder.build();
        ConfigurableApplicationContext configuration = application.run(new String[]{
                "--id=" + id
                , "--spring.profiles.active=" + id
        });
        return configuration;
    }

    @Autowired
    private Environment env;

    @Autowired
    private ConfigurableApplicationContext configuration;

    @Value("${id}")
    private String id;

    @Bean
    public Integer tableIncrement(DataSource dataSource) {
        int r = 0;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            PackageScanner scanner = new PackageScannerImpl(true);
            for (Class<?> dclass : scanner.scan(KZeroSpringStarter.class, KZeroSpringStarter.class.getClassLoader(),
                    "io.github.kloping.kzero.spring.dao")) {
                try {
                    String sql = Utils.CreateTable.createTable(dclass);
                    int state = jdbcTemplate.update(sql);
                    if (state > 0) {
                        r++;
                        System.out.println(sql);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
