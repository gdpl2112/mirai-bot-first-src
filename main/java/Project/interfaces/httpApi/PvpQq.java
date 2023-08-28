package Project.interfaces.httpApi;

import Project.commons.apiEntitys.pvpqq.Heroes;
import Project.commons.apiEntitys.pvpqq.pvpQQVoice.HeroVoice;
import Project.commons.apiEntitys.pvpqq.pvpSkin.PvpSkin;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PathValue;

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
     * @param id
     * @return
     */
    @GetPath("zlkdatasys/yuzhouzhan/herovoice/{id}.json")
    HeroVoice voice(@PathValue("id") String id);

    /**
     * get data has hero id
     *
     * @param createHeroList
     * @return
     */
    @GetPath("zlkdatasys/yuzhouzhan/list/heroList.json")
    Heroes heroList();

    /**
     * get all skin
     *
     * @return
     */
    @GetPath("zlkdatasys/data_zlk_xpflby.json")
    PvpSkin getSkins();
}