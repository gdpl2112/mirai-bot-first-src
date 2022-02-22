package Project.interfaces.http_api;

import io.github.kloping.mirai0.Entitys.apiEntitys.apiIyk0.Jyu;
import io.github.kloping.mirai0.Entitys.apiEntitys.colb.PickupABottle;
import io.github.kloping.mirai0.Entitys.apiEntitys.thb.ThrowABottle;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github-kloping
 */
@HttpClient("https://api.iyk0.com/")
public interface ApiIyk0 {
    @GetPath("jyu")
    Jyu getJyu();

    @GetPath("drift")
    ThrowABottle throwABottle(@ParamName("type") Integer one, @ParamName("msg") String msg
            , @ParamName("uin") Long uin, @ParamName("group") Object group);
    @GetPath("drift")
    PickupABottle pickupABottle(@ParamName("type") Integer two);
}
