package Project.controllers.plugins;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.apiEntitys.Song;
import Project.commons.apiEntitys.Songs;
import Project.plugins.SearchSong;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

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
        SB.append("8，点歌 歌名 #可听VIP").append("\r\n");
    }

    public PointSongController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public static void sing(String name, SpGroup group) {
        Songs songs = searchSong.kugou(name);
        MessageUtils.INSTANCE.sendVoiceMessageInGroup(songs.getData()[0].getSongUrl(), group.getId());
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("点歌系统")
    public String menu() {
        return SB.toString();
    }

    @Action("点歌<.+=>name>")
    public void pointSong(@Param("name") String name, SpUser qq, SpGroup gro) {
        Songs songs = searchSong.normal(name);
        Group group = BOT.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.top", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.QQMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.top", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("QQ点歌<.+=>name>")
    public void pointSongQQ(@Param("name") String name, SpUser qq, SpGroup gro) {
        Songs songs = searchSong.qq(name);
        Group group = BOT.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.top", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.QQMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.top", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("酷狗点歌<.+=>name>")
    public void pointSongKugou(@Param("name") String name, SpUser qq, SpGroup gro) {
        Songs songs = searchSong.kugou(name);
        Group group = BOT.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.KugouMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.top", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.KugouMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.top", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("网易点歌<.+=>name>")
    public void pointSongNetEase(@Param("name") String name, SpUser qq, SpGroup gro) {
        Songs songs = searchSong.netEase(name);
        Group group = BOT.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://kloping.top", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.NeteaseCloudMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://kloping.top", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("QQ歌词<.+=>name>")
    public Object mq(@Param("name") String name, SpGroup group) {
        try {
            Songs songs = searchSong.qq(name);
            String lyric = songs.getData()[0].getLyric();
            MessageUtils.INSTANCE.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("酷狗歌词<.+=>name>")
    public Object mk(@Param("name") String name, SpGroup group) {
        try {
            Songs songs = searchSong.kugou(name);
            String lyric = songs.getData()[0].getLyric();
            MessageUtils.INSTANCE.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }

    @Action("网易歌词<.+=>name>")
    public Object mw(@Param("name") String name, SpGroup group) {
        try {
            Songs songs = searchSong.netEase(name);
            String lyric = songs.getData()[0].getLyric();
            MessageUtils.INSTANCE.sendMessageByForward(group.getId(), lyric.split("\r|\n"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "歌词获取失败";
        }
    }
}
