package Project.aSpring;

import Project.aSpring.mcs.save.SaveMapper;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.kloping.MySpringTool.StarterApplication;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static Project.aSpring.SpringBootResource.configuration;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs.save"})
public class SaverSpringStarter {

    public static SaveMapper saveMapper;

    public static void main(String[] args) {
        try {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "--spring.config.location=./spring/conf/application0.yml";
            configuration = new SpringApplication(new Class[]{SaverSpringStarter.class}).run(args);
            SqlSessionTemplate sqlSessionTemplate = configuration.getBean(SqlSessionTemplate.class);
            MybatisConfiguration c0 = new MybatisConfiguration();
            c0.addMapper(SaveMapper.class);
            saveMapper = c0.getMapper(SaveMapper.class, sqlSessionTemplate);
            System.err.println("saver spring started succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
