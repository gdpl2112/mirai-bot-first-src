package Project.detailPlugin;

import io.github.kloping.mirai0.Entitys.apiEntitys.Songs;
import Project.interfaces.GetSongs;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;


/**
 * @author github-kloping
 */
@Entity
public class SearchSong {

    @AutoStand
    GetSongs getSongs;

    public Songs netEase(String name) {
        return getSongs.getSongs(name, "wy");
    }

    public Songs kugou(String name) {
        try {
            Songs songs = getSongs.getSongs(name, "kugou");
            return songs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Songs qq(String name) {
        return getSongs.getSongs(name, "qq");
    }
}