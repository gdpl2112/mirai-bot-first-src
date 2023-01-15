package Project.aSpring;

import Project.aSpring.mcs.save.SaveMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static Project.aSpring.SpringBootResource.configuration;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs.save"})
@MapperScan("Project.aSpring.mcs.save")
public class SaverSpringStarter {

    public static SaveMapper saveMapper;

    public static void main(String[] args) {
        try {
            SpringApplication application = new SpringApplication(SaverSpringStarter.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            configuration = application.run(args);
            saveMapper = configuration.getBean(SaveMapper.class);
            System.err.println("saver spring started succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
