package Project.interfaces.http_api;

import Project.detailPlugin.NetMain;
import io.github.kloping.MySpringTool.annotations.http.DefaultValue;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.apiEntitys.PicData;
import io.github.kloping.mirai0.commons.apiEntitys.Songs;
import io.github.kloping.mirai0.commons.apiEntitys.WeatherDetail;
import io.github.kloping.mirai0.commons.apiEntitys.WeatherM;
import io.github.kloping.mirai0.commons.apiEntitys.kloping.VideoAnimeSource;

/**
 * @author github.kloping
 */
@HttpClient(NetMain.ROOT_PATH)
public interface KlopingWeb {
    /**
     * 获取歌曲
     *
     * @param keyword
     * @param type
     * @return
     */
    @GetPath("/api/search/song")
    Songs getSongs(@ParamName("keyword") String keyword, @ParamName("type") String type);

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

    /**
     * 短时天气
     *
     * @param address
     * @return
     */
    @GetPath("/api/get/shortWeather")
    WeatherM weatherM(@ParamName("address") String address);

    /**
     * 详细天气
     *
     * @param address
     * @return
     */
    @GetPath("/api/get/weather")
    WeatherDetail weatherDetail(@ParamName("address") String address);

    /**
     * 获取视频
     *
     * @param keyword
     * @param type
     * @return
     */
    @GetPath("api/search/video")
    VideoAnimeSource[] videoSearch(@ParamName("keyword") String keyword
            , @ParamName("type") String type
    );

    /**
     * 获取视频
     *
     * @param keyword
     * @param type
     * @param url
     * @return
     */
    @GetPath("api/search/video")
    VideoAnimeSource[] videoSearch(@ParamName("keyword") String keyword
            , @ParamName("type") String type
            , @ParamName("url") String url
    );

    /**
     * 验证
     *
     * @param code
     * @return
     */
    @GetPath("/verify0")
    String verify0(@ParamName("code") String code);
}
