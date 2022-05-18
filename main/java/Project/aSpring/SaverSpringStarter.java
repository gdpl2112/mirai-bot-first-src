package Project.aSpring;

import Project.aSpring.mcs.save.SaveMapper;
import io.github.kloping.MySpringTool.StarterApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "--spring.config.location=./spring/conf/application0.yml";
            configuration = new SpringApplication(new Class[]{SaverSpringStarter.class}) {
                @Override
                public ClassLoader getClassLoader() {
                    return StarterApplication.SCAN_LOADER;
                }

                @Override
                public Set<Object> getAllSources() {
                    Set<Object> objects = new LinkedHashSet<>();
                    for (Object allSource : super.getAllSources()) {
                        objects.add(allSource);
                    }
                    objects.add(SaveMapper.class);
                    System.out.println("get all ==>>" + objects);
                    return objects;
                }
            }.run(args);
            saveMapper = configuration.getBean(SaveMapper.class);
            System.err.println("saver spring started succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
