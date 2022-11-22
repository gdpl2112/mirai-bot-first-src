package Project.aSpring.mcs.controllers;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.Resource;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;

/**
 * @author github-kloping
 */
@RestController
@Entity
public class UtilsController0 {
    @Value("${auth.pwd:123456}")
    String pwd0;

    @Value("${auth.super.pwd:123456}")
    String pwd1;

    @GetMapping("/uploadImg")
    public String uploadImg(@RequestParam("url") String url) {
        try {
            Friend friend = Resource.BOT.getBot().getAsFriend();
            Image image = Contact.uploadImage(friend, new URL(url).openStream());
            return Image.queryUrl(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://inews.gtimg.com/newsapp_bt/0/15444429680/0";
    }
}
