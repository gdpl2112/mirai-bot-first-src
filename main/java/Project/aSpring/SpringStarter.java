package Project.aSpring;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kloping.MySpringTool.StarterApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import static Project.aSpring.SpringBootResource.*;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs"})
@MapperScan("Project.aSpring.mcs.mapper")
public class SpringStarter {
    public static void main(String[] args) throws Exception {

        configuration = new SpringApplication(new Class[]{SpringStarter.class}) {
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
                for (Field declaredField : SpringBootResource.class.getDeclaredFields()) {
                    if (declaredField.getName().toLowerCase().endsWith("mapper")) {
                        objects.add(declaredField.getType());
                    }
                }
                System.out.println("get all ==>>"+objects);
                return objects;
            }
        }.run(args);
        environment = configuration.getEnvironment();
        init();
    }

    @Bean(name = "template")
    public RedisTemplate<String, Object> template(RedisConnectionFactory factory) {
        // 创建RedisTemplate<String, Object>对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        // 定义Jackson2JsonRedisSerializer序列化对象
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        StringRedisSerializer stringSerial = new StringRedisSerializer();
        // redis key 序列化方式使用stringSerial
        template.setKeySerializer(stringSerial);
        // redis value 序列化方式使用jackson
        template.setValueSerializer(jacksonSeial);
        // redis hash key 序列化方式使用stringSerial
        template.setHashKeySerializer(stringSerial);
        // redis hash value 序列化方式使用jackson
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }
}
