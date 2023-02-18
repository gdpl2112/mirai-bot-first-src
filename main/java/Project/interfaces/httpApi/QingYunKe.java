package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.DefaultValue;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.apiEntitys.qingyunke.QingYunKeData;

/**
 * @author github.kloping
 */
@HttpClient("http://api.qingyunke.com/")
public interface QingYunKe {
    /**
     * http://api.qingyunke.com/api.php?key=free&appid=0&msg=%E4%BD%A0%E5%A5%BD
     *
     * @param key
     * @param appid
     * @param message
     * @return
     */
    @GetPath("api.php")
    QingYunKeData data(
            @ParamName("key") @DefaultValue("free") String key,
            @ParamName("appid") @DefaultValue("0") Integer appid,
            @ParamName("msg") String message
    );
}
