package Project.interfaces.httpApi.old;

import io.github.kloping.MySpringTool.annotations.http.HttpClient;

/**
 * @author github-kloping
 */
@HttpClient("https://api.iyk0.com")
public interface ApiIyk0 {

    /**
     * 最新疫情
     *
     * @param address
     * @return
     */
//    @GetPath("yq")
//    YiQing yq(@ParamName("msg") String address);
//
    /**
     * 随机头像
     *
     * @param msg
     * @return
     */
//    @GetPath("sjtx")
//    JSONObject sjtx(@ParamName("msg") String msg);
//
    /**
     * 降雨图
     *
     * @return
     */
//    @GetPath("jyu")
//    Jyu getJyu();
//
    /**
     * 扔漂流瓶
     *
     * @param one
     * @param msg
     * @param uin
     * @param group
     * @return
     */
//    @GetPath("drift")
//    ThrowABottle throwBottle(@ParamName("type") Integer one, @ParamName("msg") String msg
//            , @ParamName("uin") Long uin, @ParamName("group") Object group);
//
    /**
     * 捡起漂流瓶
     *
     * @param two
     * @return
     */
//    @GetPath("drift")
//    PickupABottle pickupBottle(@ParamName("type") Integer two);
//
    /**
     * get image by name
     *
     * @param msg
     * @return
     */
//    @GetPath("swt")
//    String getImgFromName(@ParamName("msg") String msg);
//
    /**
     * get ip
     *
     * @param ip
     * @return
     */
//    @GetPath("ip")
//    String getAddressByIp(@ParamName("ip") String ip);
}
