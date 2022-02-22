package Project.controllers.Plugins;

import io.github.kloping.mirai0.Entitys.User;
import io.github.kloping.mirai0.Entitys.apiEntitys.Song;
import io.github.kloping.mirai0.Entitys.apiEntitys.Songs;
import io.github.kloping.mirai0.Entitys.apiEntitys.reping163.Reping163;
import Project.detailPlugin.SearchSong;
import Project.interfaces.http_api.MuXiaoGuo;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

import static Project.controllers.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.Resource.bot;
import static io.github.kloping.mirai0.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class PointSongController {
    public PointSongController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(io.github.kloping.mirai0.Entitys.Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    public static void sing(String name, io.github.kloping.mirai0.Entitys.Group group) {
        Songs songs = searchSong.kugou(name);
        MessageTools.sendVoiceMessageInGroup(songs.getData()[0].getSongUrl(), group.getId());
    }

    @Action("点歌系统")
    public String menu() {
        return SB.toString();
    }

    @AutoStand
    static SearchSong searchSong;

    @AutoStand
    MuXiaoGuo muXiaoGuo;

    @Action("QQ点歌<.+=>name>")
    public void pointSongQQ(@Param("name") String name, User qq, io.github.kloping.mirai0.Entitys.Group gro) {
        Songs songs = searchSong.qq(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.QQMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("酷狗点歌<.+=>name>")
    public void pointSongKugou(@Param("name") String name, User qq, io.github.kloping.mirai0.Entitys.Group gro) {
        Songs songs = searchSong.kugou(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.KugouMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.KugouMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("网易点歌<.+=>name>")
    public void pointSongNetEase(@Param("name") String name, User qq, io.github.kloping.mirai0.Entitys.Group gro) {
        Songs songs = searchSong.netEase(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.NeteaseCloudMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final StringBuilder SB = new StringBuilder();

    static {
        SB.append("1，QQ点歌 歌名").append("\r\n");
        SB.append("2，酷狗点歌 歌名").append("\r\n");
        SB.append("3，网易点歌 歌名").append("\r\n");
        SB.append("4，网易云热评 ").append("\r\n");
        SB.append("5，QQ歌词 歌名").append("\r\n");
        SB.append("6，酷狗歌词 歌名").append("\r\n");
        SB.append("7，网易歌词 歌名").append("\r\n");
    }


    @Action("网易云热评")
    public String reping163(io.github.kloping.mirai0.Entitys.Group gro) {
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
                MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180", s1.getImgUrl(), s1.getSongUrl());
                group.sendMessage(share1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }
}
