package Project.detailPlugin;

import Project.interfaces.http_api.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.apiEntitys.Songs;


/**
 * @author github-kloping
 */
@Entity
public class SearchSong {

    @AutoStand
    private KlopingWeb getSongs;

    public Songs netEase(String name) {
        return getSongs.getSongs(name, "wy",2);
    }

    public Songs kugou(String name) {
        try {
            Songs songs = getSongs.getSongs(name, "kugou",2);
            return songs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Songs qq(String name) {
        return getSongs.getSongs(name, "qq",2);
    }
}