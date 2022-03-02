package Project.aSpring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static Project.aSpring.SpringBootResource.*;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs"})
@MapperScan("Project.aSpring.mcs")
public class SpringStarter {
    public static void main(String[] args) throws Exception {
        configuration = SpringApplication.run(SpringStarter.class, args);
        environment = configuration.getEnvironment();
        init();
    }
}
