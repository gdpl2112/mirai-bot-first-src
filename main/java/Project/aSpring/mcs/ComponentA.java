package Project.aSpring.mcs;

import Project.aSpring.SpringStarter;
import io.github.kloping.MySpringTool.annotations.Entity;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author HRS-Computer
 */
public class ComponentA  {

    /**
     * @Postcontruct’在依赖注入完成后自动调用
     */
    @PostConstruct
    public void postConstruct() {

    }
}