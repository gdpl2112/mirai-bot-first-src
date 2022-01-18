package io.github.kloping.Mirai.Main.Handlers;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.Mirai.Main.Resource;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * @author github-kloping
 */
public class MyTimer {
    private static int t1 = 24;
    public static final List<Runnable> ZERO_RUNS = new ArrayList<>();

    public static void appendOneDay(MessageChainBuilder builder, Group group) {
        try {
            URL url = new URL("http://open.iciba.com/dsapi");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = br.readLine();
            JSONObject object = (JSONObject) JSONObject.parse(str);
            builder.append("\r\n 每日一句");
            builder.append("\r\n" + object.getString("note"));
            builder.append("\r\n" + object.getString("content"));
            builder.append("\r\n\t====>" + object.getString("dateline"));
            builder.append(Contact.uploadImage(group, new URL(object.getString("picture4")).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
