package Project.detailPlugin;

import Entitys.apiEntitys.Songs;

import static Project.Controllers.ControllerSource.GET_SONGS;


/**
 * @author github-kloping
 */
public class SearchSong {

    public static Songs netEase(String name) {
        return GET_SONGS.getSongs(name, "wy");
    }

    public static Songs kugou(String name) {
        try {
            Songs songs = GET_SONGS.getSongs(name, "kugou");
            return songs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Songs qq(String name) {
        return GET_SONGS.getSongs(name, "qq");
    }
}