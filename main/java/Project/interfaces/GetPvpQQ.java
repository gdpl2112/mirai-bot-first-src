package Project.interfaces;

import io.github.kloping.mirai0.Entitys.apiEntitys.pvpQqCom.Response0;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.MySpringTool.entity.Params;


@HttpClient("https://apps.game.qq.com")
public interface GetPvpQQ {
    @GetPath("cmc/cross")
    Response0 get(Params params);

    @GetPath("wmp/v3.1/public/searchNews.php")
    String get0(@ParamName("p0") int p0, @ParamName("source") String source, @ParamName("id") long id);
}
