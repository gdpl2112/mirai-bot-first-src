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
public class SpringStarter2 {
    public static void main(String[] args) {
        try {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "--spring.config.location=./spring/conf/application2.yml";
            configuration = SpringApplication.run(SpringStarter2.class, args);
            environment = configuration.getEnvironment();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
