package Project;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import io.github.kloping.MySpringTool.annotations.Bean;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.apiEntitys.RunnableWithOver;
import io.ktor.util.collections.ConcurrentSet;

import java.util.*;

import static io.github.kloping.mirai0.Main.Resource.THREADS;

/**
 * @author github-kloping
 */
@Entity
public class InitBeans {

    @Bean("m100")
    public Set<RunnableWithOver> m500() {
        Set<RunnableWithOver> rs = new ConcurrentSet<>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    rs.forEach(r -> THREADS.submit(r));
                    Iterator<RunnableWithOver> iterator = rs.iterator();
                    while (iterator.hasNext()) {
                        RunnableWithOver o = iterator.next();
                        if (o == null || o.over()) {
                            rs.remove(o);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100, 100);
        return rs;
    }

    @Bean("dataPath")
    public String dataPath() {
        return Resource.datePath;
    }

    @Bean
    public DefaultKaptcha m() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "black");
        //边框厚度
        properties.setProperty("kaptcha.border.thickness", "1");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "120");
        // 图片高
        properties.setProperty("kaptcha.image.height", "50");
        //图片实现类
        properties.setProperty("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha");
        //文本实现类
        properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
        //文本集合，验证码值从此集合中获取
        properties.setProperty("kaptcha.textproducer.char.string", "01234567890");
        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "WHITE");
        //文字间隔
        properties.setProperty("kaptcha.textproducer.char.space", "7");
        //干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        //干扰颜色
        properties.setProperty("kaptcha.noise.color", "green");
        //干扰图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        //背景实现类
        properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        //背景颜色渐变，结束颜色
        properties.setProperty("kaptcha.background.clear.to", "black");
        //文字渲染器
        properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
