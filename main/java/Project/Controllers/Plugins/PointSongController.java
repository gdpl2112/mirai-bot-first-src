package Project.Controllers.Plugins;

import Entitys.Song;
import Entitys.Songs;
import Entitys.User;
import Project.Plugins.SearchSong;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.bot;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class PointSongController {
    public PointSongController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Entitys.Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @Action("点歌系统")
    public String Menu() {
        return sb.toString();
    }

    @Action("QQ点歌<.+=>name>")
    public void PointSongQQ(@Param("name") String name, User qq, Entitys.Group gro) {
        Songs songs = SearchSong.Qq(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.QQMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180:20041/", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.QQMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180:20041/", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("酷狗点歌<.+=>name>")
    public void PointSongKugou(@Param("name") String name, User qq, Entitys.Group gro) {
        Songs songs = SearchSong.Kugou(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.KugouMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180:20041/", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.KugouMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180:20041/", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("网易点歌<.+=>name>")
    public void PointSongNetEase(@Param("name") String name, User qq, Entitys.Group gro) {
        Songs songs = SearchSong.NetEase(name);
        Group group = bot.getGroup(gro.getId());
        Song s1 = songs.getData()[0];
        MusicShare share1 = new MusicShare(MusicKind.NeteaseCloudMusic, s1.getMedia_name(), s1.getAuthor_name(), "http://49.232.209.180:20041/", s1.getImgUrl(), s1.getSongUrl());
        group.sendMessage(share1);
        try {
            Song s2 = songs.getData()[1];
            MusicShare share2 = new MusicShare(MusicKind.NeteaseCloudMusic, s2.getMedia_name(), s2.getAuthor_name(), "http://49.232.209.180:20041/", s2.getImgUrl(), s2.getSongUrl());
            group.sendMessage(share2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final StringBuilder sb = new StringBuilder();

    static {
        sb.append("1，QQ点歌 歌名").append("\r\n");
        sb.append("2，酷狗点歌 歌名").append("\r\n");
        sb.append("3，网易点歌 歌名").append("\r\n");
    }
}
