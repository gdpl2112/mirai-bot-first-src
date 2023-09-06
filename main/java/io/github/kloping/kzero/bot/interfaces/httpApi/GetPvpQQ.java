package io.github.kloping.kzero.bot.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.MySpringTool.entity.Params;
import io.github.kloping.kzero.bot.commons.apis.pvpqq.pvpQqCom.Response0;


/**
 * @author github-kloping
 */
@HttpClient("https://apps.game.qq.com")
public interface GetPvpQQ {
    /**
     * get
     *
     * @param params
     * @return
     */
    @GetPath("cmc/cross")
    Response0 get(Params params);

    /**
     * get 0
     *
     * @param p0
     * @param source
     * @param id
     * @return
     */
    @GetPath("wmp/v3.1/public/searchNews.php")
    String get0(@ParamName("p0") int p0, @ParamName("source") String source, @ParamName("id") long id);
}
