package Project.interfaces.httpApi;

import Project.commons.apiEntitys.*;
import Project.commons.apiEntitys.kloping.VideoAnimeSource;
import Project.plugins.NetMain;
import io.github.kloping.MySpringTool.annotations.http.*;
import io.github.kloping.mirai0.commons.entity.PayOut;

import java.util.List;
import java.util.Map;

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
     * @param n
     * @return
     */
    @GetPath("/api/search/song")
    Songs getSongs(@ParamName("keyword") String keyword, @ParamName("type") String type, @ParamName("n") Integer n);

    /**
     * 获取VIP歌曲
     *
     * @param keyword
     * @param n
     * @return
     */
    @GetPath("/api/search/vipSong")
    Songs getVipSongs(@ParamName("keyword") String keyword, @ParamName("n") Integer n);

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
     * 解析图音频
     *
     * @param url
     * @return
     */
    @GetPath("/api/search/parseVoice")
    String parsePic(@ParamName("url") String url);

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
    VideoAnimeSource videoSearch(@ParamName("keyword") String keyword
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

    /**
     * uploadImg
     *
     * @param headers
     * @param key
     * @param body
     * @return
     */
    @PostPath("/uploadImg")
    String uploadImg(@Headers Map<String, String> headers, @ParamName("key") String key, @RequestBody String body);


    /**
     * throw
     *
     * @param gid
     * @param sid
     * @param message
     * @param name
     * @return
     */
    @GetPath("/api/throwBottle")
    String throwBottle(
            @ParamName("gid") Long gid,
            @ParamName("sid") Long sid,
            @ParamName("message") String message,
            @ParamName("name") String name
    );

    /**
     * pickUP
     *
     * @return
     */
    @GetPath("/api/pickUpBottle")
    BottleMessage pickUpBottle();

    /**
     * save a value by pwd and key
     *
     * @param key
     * @param value
     * @param pwd
     * @return
     */
    @GetPath("/put")
    String put(@ParamName("key") String key, @ParamName("value") String value, @ParamName("pwd") String pwd);

    /**
     * get num from
     *
     * @param keys
     * @param value
     * @param pwd
     * @return
     */
    @GetPath("/containsKeys")
    Integer containsKeys(@ParamName("keys") String keys, @ParamName("value") String value, @ParamName("pwd") String pwd);

    /**
     * get num from
     *
     * @param keys
     * @param value
     * @param pwds
     * @return
     */
    @GetPath("/containsPwds")
    String containsPwds(@ParamName("key") String key, @ParamName("value") String value, @ParamName("pwds") String pwds);

    /**
     * get a value by pwd and key
     *
     * @param key
     * @param pwd
     * @return
     */
    @GetPath("/get")
    String get(@ParamName("key") String key, @ParamName("pwd") String pwd);

    /**
     * delete a/all value by pwd or and key
     *
     * @param key
     * @param pwd
     * @return
     */
    @GetPath("/del")
    String del(@ParamName("key") String key, @ParamName("pwd") String pwd);

    /**
     * pa
     *
     * @param qq
     * @return
     */
    @GetPath("/api/image/pa")
    String pa(@ParamName("qid") Long qq);

    /**
     * a
     *
     * @param skey
     * @param pskey
     * @param uin
     * @param qq
     * @param select
     * @param jie
     * @param title
     * @param payId
     * @param gid
     * @return
     */
    @GetPath("/api/group/pay")
    PayOut pay(@ParamName("skey") String skey,
               @ParamName("pskey") String pskey,
               @ParamName("uin") Long uin,
               @ParamName("qq") Long qq,
               @ParamName("select") Integer select,
               @ParamName("jie") Float jie,
               @ParamName("title") String title,
               @ParamName("payId") String payId,
               @ParamName("gid") Long gid);

    /**
     * ocr识别
     *
     * @param url
     * @return
     */
    @GetPath("/api/ocr")
    String ocr(@ParamName("url") String url);
}
