package Project.aSpring;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static Project.aSpring.SpringBootResource.*;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs"})
@MapperScan("Project.aSpring.mcs.mapper")
public class SpringStarter {

    public static ConfigurableApplicationContext configuration;

    public static void main(String[] args) {
        configuration = new SpringApplication(new Class[]{SpringStarter.class}).run(args);
        environment = configuration.getEnvironment();
        init();
        over();
    }

    private static void over() {
        // 获取配置的数据源
        DataSource dataSource = SpringStarter.configuration.getBean(DataSource.class);
        //执行SQL,输出查到的数据
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        boolean k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `zong`")) {
            String name = e0.get("Field").toString();
            if ("active".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("zong添加字段");
            jdbcTemplate.update("ALTER TABLE `zong` ADD COLUMN `active` INT(9) NOT NULL COMMENT '活跃点数' AFTER `pub`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `zon`")) {
            String name = e0.get("Field").toString();
            if ("active".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("zon添加字段");
            jdbcTemplate.update("ALTER TABLE `zon` ADD COLUMN `active` INT NOT NULL COMMENT '活跃点数' AFTER `xper`;");
        }
    }
}
