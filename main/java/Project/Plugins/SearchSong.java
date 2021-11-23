package Project.Plugins;

import Entitys.apiEntitys.Songs;

import static Project.Controllers.FirstController.getSong;

public class SearchSong {

    public static Songs NetEase(String name) {
        return getSong.getSongs(name, "wy");
    }

    public static Songs Kugou(String name) {
        return getSong.getSongs(name, "kugou");
    }

    public static Songs Qq(String name) {
        return getSong.getSongs(name, "qq");
    }
}