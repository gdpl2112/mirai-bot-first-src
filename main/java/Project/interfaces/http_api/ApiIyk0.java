package Project.interfaces.http_api;

import io.github.kloping.mirai0.commons.apiEntitys.apiIyk0.Jyu;
import io.github.kloping.mirai0.commons.apiEntitys.colb.PickupABottle;
import io.github.kloping.mirai0.commons.apiEntitys.thb.ThrowABottle;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github-kloping
 */
@HttpClient("https://api.iyk0.com")
public interface ApiIyk0 {
    /**
     * 降雨图
     *
     * @return
     */
    @GetPath("jyu")
    Jyu getJyu();

    /**
     * 扔漂流瓶
     *
     * @param one
     * @param msg
     * @param uin
     * @param group
     * @return
     */
    @GetPath("drift")
    ThrowABottle throwBottle(@ParamName("type") Integer one, @ParamName("msg") String msg
            , @ParamName("uin") Long uin, @ParamName("group") Object group);

    /**
     * 捡起漂流瓶
     *
     * @param two
     * @return
     */
    @GetPath("drift")
    PickupABottle pickupBottle(@ParamName("type") Integer two);

    /**
     * get image by name
     *
     * @param msg
     * @return
     */
    @GetPath("swt")
    String getImgFromName(@ParamName("msg") String msg);
}
