package Project.detailPlugin;

import Entitys.apiEntitys.Songs;

import static Project.Controllers.ControllerSource.getSong;


/**
 * @author github-kloping
 */
public class SearchSong {

    public static Songs netEase(String name) {
        return getSong.getSongs(name, "wy");
    }

    public static Songs kugou(String name) {
        try {
            Songs songs = getSong.getSongs(name, "kugou");
            return songs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Songs qq(String name) {
        return getSong.getSongs(name, "qq");
    }
}