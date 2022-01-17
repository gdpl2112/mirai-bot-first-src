package Project.detailPlugin;

import Entitys.apiEntitys.Songs;

import static Project.Controllers.ConUtils.getSong;


/**
 * @author github-kloping
 */
public class SearchSong {

    public static Songs netEase(String name) {
        return getSong.getSongs(name, "wy");
    }

    public static Songs kugou(String name) {
        return getSong.getSongs(name, "kugou");
    }

    public static Songs qq(String name) {
        return getSong.getSongs(name, "qq");
    }
}