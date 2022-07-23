package Project.detailPlugin;

import Project.interfaces.http_api.KlopingWeb;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.io.IOException;


/**
 * @author github-kloping
 */
@Entity
public class SearchPic {

    @AutoStand
    static KlopingWeb searchPics;

    public String[] getPic(String name) throws IOException {
        return searchPics.getPicNum(name, 6, null).getData();
    }

    public String[] getPicDt(String name) throws IOException {
        return searchPics.getPicNum(name, null, "duit").getData();
    }

    public String[] getPicM(String name) throws Exception {
        return searchPics.getPicNum(name, 19, "baidu").getData();
    }

    public String[] parseKsImgs(String urlStr) throws IOException {
        return searchPics.parsePic(urlStr, null);
    }

    public String[] parseDyImgs(String urlStr) throws IOException {
        return searchPics.parsePic(urlStr, "dy");
    }
}