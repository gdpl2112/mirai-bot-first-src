package Project.interfaces;

import Entitys.apiEntitys.Jyu;
import Entitys.apiEntitys.colb.PickupABottle;
import Entitys.apiEntitys.thb.ThrowABottle;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

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
