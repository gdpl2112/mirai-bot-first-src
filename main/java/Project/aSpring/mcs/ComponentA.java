package Project.aSpring.mcs;

import Project.aSpring.SpringBootResource;
import org.apache.catalina.core.ApplicationContext;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author HRS-Computer
 */
@Component
public class ComponentA {

    /**
     * @Postcontruct’在依赖注入完成后自动调用
     */
    @PostConstruct
    public void postConstruct() {
        // 获取配置的数据源
        DataSource dataSource = SpringBootResource.configuration.getBean(DataSource.class);
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