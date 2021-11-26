package Project.ASpring;

import Project.ASpring.mapper.PersonInfoMapper;
import Project.ASpring.mapper.UScoreMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.Array;
import java.util.Arrays;

import static Project.ASpring.SpringBootResource.*;

@SpringBootApplication(scanBasePackages = {"Project.ASpring.mapper"})
@MapperScan("Project.ASpring.mapper")
@PropertySource(value = {"file:./application2.yml"})
public class SpringStarter2 {
    public static void main(String[] args) {
        args = new String[]{
                "--spring.config.location=./spring/conf/application2.yml"
        };
        configuration = SpringApplication.run(SpringStarter2.class, args);
        environment = configuration.getEnvironment();
        System.out.println("==============spring papered=================");
        scoreMapper = configuration.getBean(UScoreMapper.class);
        personInfoMapper = configuration.getBean(PersonInfoMapper.class);
    }
}
