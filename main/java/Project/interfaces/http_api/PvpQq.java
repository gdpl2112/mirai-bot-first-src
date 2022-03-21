package Project.interfaces.http_api;

import io.github.kloping.mirai0.commons.apiEntitys.pvpQQH0.PvpQQH0;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQQVoice.PvpQQVoice;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.apiEntitys.pvpSkin.PvpSkin;

/**
 * @author github kloping
 * @version 1.0
 * @date 2021/12/30-9:54
 */
@HttpClient("https://pvp.qq.com/")
public interface PvpQq {
    /**
     * get data voice
     *
     * @param createList
     * @return
     */
    @GetPath("zlkdatasys/data_zlk_lb.json")
    @Callback("Project.detailPlugin.PvpQq.c1")
    PvpQQVoice get0(@ParamName("callback") String createList);

    /**
     * get data has hero id
     *
     * @param createHeroList
     * @return
     */
    @GetPath("webplat/info/news_version3/15592/18024/23901/24397/24398/m22352/index.shtml?callback=createHeroList")
    @Callback("Project.detailPlugin.PvpQq.c1")
    PvpQQH0 get1(@ParamName("callback") String createHeroList);

    /**
     * get all skin
     *
     * @return
     */
    @GetPath("zlkdatasys/data_zlk_xpflby.json")
    PvpSkin getSkins();
}