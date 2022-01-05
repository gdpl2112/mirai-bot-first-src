package Project.Plugins;

import java.io.IOException;

import static Project.Controllers.FirstController.searchPics;

/**
 * @author github-kloping
 */
public class SearchPic {

    public static String[] getPic(String name) throws IOException {
        return searchPics.getPicNum(name, 6, null).getData();
    }

    public static String[] getPicDt(String name) throws IOException {
        return searchPics.getPicNum(name, null, "duit").getData();
    }

    public static String[] getPicM(String name) throws Exception {
        return searchPics.getPicNum(name, 19, "baidu").getData();
    }

    public static String[] parseKsImgs(String urlStr) throws IOException {
        return searchPics.parsePic(urlStr, null);
    }

    public static String[] parseDyImgs(String urlStr) throws IOException {
        return searchPics.parsePic(urlStr, "dy");
    }
}