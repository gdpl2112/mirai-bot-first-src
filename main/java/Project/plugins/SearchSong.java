package Project.plugins;

import Project.interfaces.httpApi.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import Project.commons.apiEntitys.Songs;


/**
 * @author github-kloping
 */
@Entity
public class SearchSong {

    @AutoStand
    private KlopingWeb getSongs;

    public Songs normal(String name) {
        return getSongs.getVipSongs(name, 2);
    }

    public Songs netEase(String name) {
        return getSongs.getSongs(name, "wy", 2);
    }

    public Songs kugou(String name) {
        try {
            Songs songs = getSongs.getSongs(name, "kugou", 2);
            return songs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Songs qq(String name) {
        return getSongs.getSongs(name, "qq", 2);
    }
}