package Project.ASpring;

import Project.ASpring.mcs.save.SaveMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static Project.ASpring.SpringBootResource.*;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.ASpring.mcs.save"})
@MapperScan("Project.ASpring.mcs.save")
public class SpringStarter0 {
    public static SaveMapper saveMapper;

    public static void main(String[] args) {
        try {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "--spring.config.location=./spring/conf/application0.yml";
            configuration = SpringApplication.run(SpringStarter.class, args);

            saveMapper = configuration.getBean(SaveMapper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
