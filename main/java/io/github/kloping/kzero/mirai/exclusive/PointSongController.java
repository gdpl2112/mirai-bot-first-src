package io.github.kloping.kzero.mirai.exclusive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.kzero.bot.KlopingWeb;
import io.github.kloping.kzero.bot.commons.apis.Song;
import io.github.kloping.kzero.bot.commons.apis.Songs;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.main.api.MessageType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author github-kloping
 */
@Controller
public class PointSongController {
    @Before
    public void before(@AllMess String msg, KZeroBot kZeroBot, MessagePack pack) {
        if (!(kZeroBot.getSelf() instanceof Bot)) throw new NoRunException("mirai-bot专属扩展");
    }

    private static final StringBuilder SB = new StringBuilder();

    @AutoStand
    KlopingWeb klopingWeb;

    static {
        SB.append("1，QQ点歌 歌名").append("\r\n");
        SB.append("2，酷狗点歌 歌名").append("\r\n");
        SB.append("3，网易点歌 歌名").append("\r\n");
        SB.append("4，网易云热评 ").append("\r\n");
        SB.append("5，QQ歌词 歌名").append("\r\n");
        SB.append("6，酷狗歌词 歌名").append("\r\n");
        SB.append("7，网易歌词 歌名").append("\r\n");
        SB.append("8，点歌 歌名 #可听VIP").append("\r\n");
    }

    @Action("点歌系统")
    public String menu() {
        return SB.toString();
    }

    RestTemplate template = new RestTemplate();

    @Action("点歌<.+=>name>")
    public String pointSong(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        JSONObject data = getSongData(name);
        if (data.getInteger("code") == 200) {
            String mp3 = data.getString("mp3");
            bot.getAdapter().sendMessage(MessageType.GROUP, pack.getSubjectId(), new MusicShare(
                    MusicKind.QQMusic, data.getString("name"), data.getString("author"),
                    mp3, data.getString("img"), mp3));
        } else return data.getString("msg");
        return null;
    }

    private JSONObject getSongData(String name) {
        String url = String.format("https://api.linhun.vip/api/qqyy?name=%s&y=1&n=1&apiKey=5ff26395f76d3e12b694e1875e37a40a", name.trim());
        String json = template.getForObject(url, String.class);
        JSONObject data = JSON.parseObject(json);
        return data;
    }

    @Action("歌词<.+=>name>")
    public String pl(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        JSONObject data = getSongData(name.trim());
        List<String> lines = new LinkedList<>();
        for (Object lyric : data.getJSONArray("lyric")) {
            JSONObject l0 = (JSONObject) lyric;
            String line = String.format("[%s]%s", l0.getString("time"), l0.getString("name"));
            lines.add(line);
        }
        if (data.getInteger("code") == 200) {
            bot.getAdapter().sendMessageByForward(MessageType.GROUP, pack.getSubjectId(), lines.toArray(new String[0]));
        } else return data.getString("msg");
        return null;
    }

    @Action(value = "QQ点歌<.+=>name>",otherName = {"QQ音乐点歌<.+=>name>","qq点歌<.+=>name>"})
    public String pointSongQQ(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        Songs songs = klopingWeb.getSongs(name, "qq", 1);
        if (songs != null && songs.getData().length > 0) {
            Song s1 = songs.getData()[0];
            bot.getAdapter().sendMessage(MessageType.GROUP, pack.getSubjectId(), new MusicShare(
                    MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), s1.getSongUrl(), s1.getImgUrl(), s1.getSongUrl()));
            return null;
        } else return "搜索失败!";
    }

    @Action("QQ歌词<.+=>name>")
    public Object mq(@Param("name") String name, KZeroBot bot, MessagePack pack) {
        try {
            Songs songs = klopingWeb.getSongs(name, "qq", 1);
            String lyric = songs.getData()[0].getLyric();
            bot.getAdapter().sendMessageByForward(MessageType.GROUP, pack.getSubjectId(), lyric.split("\n"));
            return null;
        } catch (Exception e) {
            return "歌词获取失败";
        }
    }

    @Action("酷狗歌词<.+=>name>")
    public Object mk(@Param("name") String name, KZeroBot bot, MessagePack pack) {
        try {
            Songs songs = klopingWeb.getSongs(name, "kugou", 1);
            String lyric = songs.getData()[0].getLyric();
            bot.getAdapter().sendMessageByForward(MessageType.GROUP, pack.getSubjectId(), lyric.split("\n"));
            return null;
        } catch (Exception e) {
            return "歌词获取失败";
        }
    }

    @Action("网易歌词<.+=>name>")
    public Object mw(@Param("name") String name, KZeroBot bot, MessagePack pack) {
        try {
            Songs songs = klopingWeb.getSongs(name, "wy", 1);
            String lyric = songs.getData()[0].getLyric();
            bot.getAdapter().sendMessageByForward(MessageType.GROUP, pack.getSubjectId(), lyric.split("\n"));
            return null;
        } catch (Exception e) {
            return "歌词获取失败";
        }
    }
}