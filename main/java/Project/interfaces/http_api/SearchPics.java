package Project.interfaces.http_api;

import io.github.kloping.mirai0.Entitys.apiEntitys.PicData;
import io.github.kloping.MySpringTool.annotations.http.DefaultValue;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

import static Project.detailPlugin.NetMain.ROOT_PATH;

/**
 * @author github kloping
 * @version 1.0
 * @Date 2022/1/5-09
 */
@HttpClient(ROOT_PATH)
public interface SearchPics {
    /**
     * 获取图片
     *
     * @param word
     * @param num
     * @param type
     * @return
     */
    @GetPath("/api/search/pic")
    PicData getPicNum(
            @ParamName("keyword") String word
            , @DefaultValue("15") @ParamName("num") Integer num
            , @DefaultValue("baidu") @ParamName("type") String type);

    /**
     * 解析图片
     *
     * @param url
     * @param type
     * @return
     */
    @GetPath("/api/search/parseImgs")
    String[] parsePic(@ParamName("url") String url, @ParamName("type") @DefaultValue("ks") String type);
}
