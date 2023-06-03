package Project.aSpring.mcs.controllers;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.BootstarpResource;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
    public String uploadImg(@RequestParam("pwd") String pwd, @RequestParam("url") String url) {
        if (!pwd.equals(pwd1)) return "";
        try {
            Friend friend = BootstarpResource.BOT.getBot().getAsFriend();
            Image image = Contact.uploadImage(friend, new URL(url).openStream());
            return Image.queryUrl(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://inews.gtimg.com/newsapp_bt/0/15444429680/0";
    }

    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("pwd") String pwd, @RequestParam("file") @Nullable MultipartFile imageFile) {
        if (!pwd.equals(pwd1)) return "";
        try {
            Friend friend = BootstarpResource.BOT.getBot().getAsFriend();
            Image image = Contact.uploadImage(friend, new ByteArrayInputStream(imageFile.getBytes()));
            return Image.queryUrl(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://inews.gtimg.com/newsapp_bt/0/15444429680/0";
    }


}
