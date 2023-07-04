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
        modify0(jdbcTemplate, "`user_score`", "who", "varchar(50)",
                "ALTER TABLE `user_score`\n" +
                        "\tALTER `who` DROP DEFAULT;\n" +
                        "ALTER TABLE `user_score`\n" +
                        "\tCHANGE COLUMN `who` `id` VARCHAR(50) NOT NULL FIRST;");

        modify0(jdbcTemplate, "`signlist`", "qid", "varchar(50)",
                "ALTER TABLE `signlist` CHANGE COLUMN `qid` `qid` VARCHAR(50) NOT NULL AFTER `id`;");


        modify0(jdbcTemplate, "`group_conf`", "id", "varchar(50)",
                "ALTER TABLE `group_conf` CHANGE COLUMN `id` `id` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`father`", "id", "varchar(50)",
                "ALTER TABLE `father` CHANGE COLUMN `id` `id` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`trading_record`", "from", "varchar(50)",
                "ALTER TABLE `trading_record` CHANGE COLUMN `from` `from` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`trading_record`", "to", "varchar(50)",
                "ALTER TABLE `trading_record` CHANGE COLUMN `to` `to` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`trading_record`", "main", "varchar(50)",
                "ALTER TABLE `trading_record` CHANGE COLUMN `main` `main` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`wh_info`", "qid", "varchar(50)",
                "ALTER TABLE `wh_info` CHANGE COLUMN `qid` `qid` VARCHAR(50) NULL DEFAULT NULL FIRST;");

        modify0(jdbcTemplate, "`upup`", "qid", "varchar(50)",
                "ALTER TABLE `upup` CHANGE COLUMN `qid` `qid` VARCHAR(50) NULL DEFAULT NULL FIRST;");

    }

    public static boolean modify0(JdbcTemplate template, String table, String field, String type, String sql) {
        List<Map<String, Object>> list =
                template.queryForList("SHOW COLUMNS FROM " + table + ";");

        for (Map<String, Object> map : list) {
            String field1 = map.get("Field").toString();
            if (field.equals(field1)) {
                String type1 = map.get("Type").toString();
                if (!type.equals(type1)) {
                    template.execute(sql);
                    System.out.println(table + "结构更新完成");
                    return true;
                }
            }
        }
        return false;
    }
}
