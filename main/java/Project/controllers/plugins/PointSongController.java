package Project.controllers.plugins;

import Project.detailPlugin.SearchSong;
import Project.interfaces.http_api.Empty;
import Project.interfaces.http_api.MuXiaoGuo;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.User;
import io.github.kloping.mirai0.commons.apiEntitys.Song;
import io.github.kloping.mirai0.commons.apiEntitys.Songs;
import io.github.kloping.mirai0.commons.apiEntitys.reping163.Reping163;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.Main.Resource.println;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;

/**
 * @author github-kloping
 */
@Controller
public class PointSongController {
    private static final StringBuilder SB = new StringBuilder();
    @AutoStand
    static SearchSong searchSong;

    static {
        SB.append("1，QQ点歌 歌名").append("\r\n");
        SB.append("2，酷狗点歌 歌名").append("\r\n");
        SB.append("3，网易点歌 歌名").append("\r\n");
        SB.append("4，网易云热评 ").append("\r\n");
        SB.append("5，QQ歌词 歌名").append("\r\n");
        SB.append("6，酷狗歌词 歌名").append("\r\n");
        SB.append("7，网易歌词 歌名").append("\r\n");
    }

    @AutoStand
    MuXiaoGuo muXiaoGuo;

    public PointSongController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static void sing(String name, io.github.kloping.mirai0.commons.Group group) {
        Songs songs = searchSong.kugou(name);
        MessageTools.sendVoiceMessageInGroup(songs.getData()[0].getSongUrl(), group.getId());
    }

    @Before
    public void before(io.github.kloping.mirai0.commons.Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("点歌系统")
    public String menu() {
        return SB.toString();
    }

    @Action("QQ点歌<.+=>name>")
    public void pointSongQQ(@Param("name") String name, User qq, io.github.kloping.mirai0.commons.Group gro) {
        Songs songs = searchSong.qq(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.life", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.QQMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.life", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("酷狗点歌<.+=>name>")
    public void pointSongKugou(@Param("name") String name, User qq, io.github.kloping.mirai0.commons.Group gro) {
        Songs songs = searchSong.kugou(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.KugouMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.life", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.KugouMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.life", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("网易点歌<.+=>name>")
    public void pointSongNetEase(@Param("name") String name, User qq, io.github.kloping.mirai0.commons.Group gro) {
        Songs songs = searchSong.netEase(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.life", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.NeteaseCloudMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.life", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("网易云热评")
    public String reping163(io.github.kloping.mirai0.commons.Group gro) {
        try {
            Reping163 reping163 = muXiaoGuo.reping();
            StringBuilder sb = new StringBuilder();
            sb.append("评论者昵称:").append(reping163.getData().getNickname()).append("\n");
            sb.append("网易云热评:\n");
            sb.append("=============\n")
                    .append(reping163.getData().getContent()).append("\n");
            sb.append("=============\n");
            sb.append("点赞数: ").append(reping163.getData().getLikedCount()).append("\n");
            sb.append("歌名:").append(reping163.getData().getSongName()).append("\n");
            sb.append("相关歌曲:").append("https://music.163.com/#/song?id=" + reping163.getData().getSongId()).append("\n");
            try {
                return sb.toString();
            } finally {
                Songs songs = searchSong.netEase(reping163.getData().getSongName());
                net.mamoe.mirai.contact.Group group = bot.getGroup(gro.getId());
                Song s1 = songs.getData()[0];
                MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.life", s1.getImgUrl(), s1.getSongUrl());
                group.sendMessage(share1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    @Action("QQ歌词<.+=>name>")
    public Object mq(@Param("name") String name, io.github.kloping.mirai0.commons.Group group) {
        try {
            Songs songs = searchSong.qq(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("酷狗歌词<.+=>name>")
    public Object mk(@Param("name") String name, io.github.kloping.mirai0.commons.Group group) {
        try {
            Songs songs = searchSong.kugou(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("网易歌词<.+=>name>")
    public Object mw(@Param("name") String name, io.github.kloping.mirai0.commons.Group group) {
        try {
            Songs songs = searchSong.netEase(name);
            String lyric = songs.getData()[0].getLyric();
            MessageTools.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    static final String BASE0 = "±img=";
    static final String BASE1 = "±";
    static final String BASE2 = "作者昵称 :";
    static final String BASE3 = "播放链接:";
    static final String BASE4 = " 歌词:";

    @AutoStand
    Empty empty;

    @Action("随机唱鸭")
    public Object cy(io.github.kloping.mirai0.commons.Group group) {
        String s0 = null;
        s0 = empty.empty("http://api.weijieyue.cn/api/changba/changya.php").body().text();
        int i1 = s0.indexOf(BASE0);
        int i2 = s0.substring(i1 + BASE0.length()).indexOf("±");
        String imgUrl = s0.substring(i1 + BASE0.length(), i2 + BASE0.length());
        s0 = s0.substring(i2 + BASE0.length());
        String aName = s0.substring(s0.indexOf(BASE2) + BASE2.length() + 1, s0.indexOf(BASE4));
        String sUrl = s0.substring(s0.indexOf(BASE3) + BASE3.length());
        MusicShare share = new MusicShare(MusicKind.QQMusic, aName, aName, sUrl, imgUrl, sUrl);
        bot.getGroup(group.getId()).sendMessage(share);
        return null;
    }
}
