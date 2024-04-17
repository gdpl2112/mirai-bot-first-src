package io.github.kloping.kzero.spring.web;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.MessageType;
import io.github.kloping.rand.RandomUtils;
import io.github.kloping.url.UrlUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ExtendController {
    public static final Set<String> CANS = new LinkedHashSet<>();
    public static final Map<String, String> CAPING = new ConcurrentHashMap<>();
    private static final Map<String, String> UCAP = new ConcurrentHashMap<>();


    @Value("${auth.pwd:123456}")
    String pwd0;
    @Value("${auth.super.pwd:123456}")
    String pwd1;
    @Value("${web.url:http://localhost}")
    String webUrl;
    @Value("${web.pwd:123456}")
    String webPwd;

    public static final String CAP_GID = "570700910";

    @PostMapping("uploadTips")
    public Object uploadTips(@RequestBody String data) {
        try {
            KlopZeroMainThreads.BOT_MAP.forEach((e, v) -> {
                v.getAdapter().sendMessage(MessageType.GROUP, CAP_GID, "有新的帖子上传成功");
            });
            data = URLDecoder.decode(data, Charset.forName("UTF-8"));
            if (!data.endsWith("}")) {
                int i = data.lastIndexOf("}");
                data = data.substring(0, i + 1);
            }
            Notice notice = JSON.parseObject(data, Notice.class);
            Document doc = Jsoup.parse(notice.getHtml());
            doc.outputSettings().prettyPrint(true);
            String ds = doc.toString();
            String finalData = data;
            KlopZeroMainThreads.BOT_MAP.forEach((e, v) -> {
                v.getAdapter().sendMessage(MessageType.GROUP, CAP_GID, finalData);
                v.getAdapter().sendMessage(MessageType.GROUP, CAP_GID, ds);
                v.getAdapter().sendMessage(MessageType.GROUP, CAP_GID, JSON.toJSONString(notice));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }


    @RequestMapping("say")
    public synchronized String say1(@RequestParam("gid") String gid,
                                    @RequestParam("pwd") String pwd,
                                    @RequestParam("s") String s) {
        if (!pwd.equals(pwd1)) return "err";
        KlopZeroMainThreads.BOT_MAP.forEach((e, v) -> {
            try {
                v.getAdapter().sendMessage(MessageType.GROUP, gid, s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return "ok";
    }

    public String accept(Integer id) {
        return UrlUtils.getStringFromHttpUrl(webUrl + "/notice/accept?id=" + id + "&pwd=" + webPwd);
    }

    public static final char[] ALL_CHARS = new char[]{
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    @AutoStand
    public static DefaultKaptcha defaultKaptcha;

    public static String getCode() {
        char[] chars = new char[4];
        for (int i = 0; i < chars.length; i++) chars[i] = ALL_CHARS[RandomUtils.RANDOM.nextInt(ALL_CHARS.length)];
        return new String(chars);
    }

    public Object[] createCapImage() {
        try {
            String caps = getCode();
            BufferedImage bi = defaultKaptcha.createImage(caps);
            File file = new File("./temp/" + UUID.randomUUID() + ".png");
            ImageIO.write(bi, "png", file);
            return new Object[]{file.getAbsolutePath(), caps, file.getName()};
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[]{e.getMessage()};
        }
    }

    /**
     * @author github-kloping
     * @date 2023-03-16
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public class Notice {
        private Long id;
        private Integer state;
        private Integer views;
        private String title;
        private String icon;
        private String date;
        private String html;
        private Long time;
        private String authorName;
        private Long authorId;
    }
}