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

//    @GetPath("shipu")
//    ShiPu getShiPu(@ParamName("key") String key, @ParamName("p") Integer page, @ParamName("n") Integer num);

    //https://api.iyk0.com/drift/?type=1&msg=内容&uin=QQ号&group=QQ群号
    @GetPath("drift")
    ThrowABottle throwABottle(@ParamName("type") Integer one, @ParamName("msg") String msg
            , @ParamName("uin") Long uin, @ParamName("group") Object group);

//    default ThrowABottle throwABottle(String msg, Long uin, Object group) {
//        return throwABottle(1, msg, uin, group);
//    }

    @GetPath("drift")
    PickupABottle pickupABottle(@ParamName("type") Integer two);

//    default PickupABottle pickupABottle() {
//        return pickupABottle(2);
//    }

}
