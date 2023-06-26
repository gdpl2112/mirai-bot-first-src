package io.github.kloping.gb.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author github.kloping
 */
@SpringBootApplication(scanBasePackages = {"io.github.kloping.gb.spring"})
@MapperScan("io.github.kloping.gb.spring")
public class MySpringApplication {

    public static ConfigurableApplicationContext configuration;
    public static ConfigurableEnvironment environment;

    public static void main(String[] args) {
        configuration = new SpringApplication(new Class[]{MySpringApplication.class}).run(args);
        environment = configuration.getEnvironment();
        over();
    }

    private static void over() {
        DataSource dataSource = MySpringApplication.configuration.getBean(DataSource.class);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try0(jdbcTemplate);
    }

    private static void try0(JdbcTemplate jdbcTemplate) {
        List<Map<String, Object>> list =
                jdbcTemplate.queryForList("SHOW COLUMNS FROM `user_score`;");
        for (Map<String, Object> map : list) {
            String field = map.get("Field").toString();
            if (field.equals("who")) {
                String type = map.get("Type").toString();
                if (!type.equals("varchar(50)")) {
                    jdbcTemplate.execute("ALTER TABLE `user_score`\n" +
                            "\tALTER `who` DROP DEFAULT;\n" +
                            "ALTER TABLE `user_score`\n" +
                            "\tCHANGE COLUMN `who` `id` VARCHAR(50) NOT NULL FIRST;");
                }
            }
        }
        System.out.println("user_score 结构更新完成");
    }
}
