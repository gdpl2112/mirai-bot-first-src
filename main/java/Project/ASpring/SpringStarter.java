package Project.ASpring;

import Project.ASpring.mapper.PersonInfoMapper;
import Project.ASpring.mapper.UScoreMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static Project.ASpring.SpringBootResource.*;

@SpringBootApplication(scanBasePackages = {"Project.ASpring.mapper"})
@MapperScan("Project.ASpring.mapper")
@Configuration
public class SpringStarter {
    public static void main(String[] args) {
        args = Arrays.copyOf(args, args.length + 1);
        args[args.length - 1] = "--spring.config.location=./spring/conf/application.yml";
        args = new String[]{
                "--spring.config.location=./spring/conf/application.yml"
        };
        configuration = SpringApplication.run(SpringStarter.class, args);
        environment = configuration.getEnvironment();
        System.out.println("==============spring papered=================");
        scoreMapper = configuration.getBean(UScoreMapper.class);
        personInfoMapper = configuration.getBean(PersonInfoMapper.class);
    }
}
