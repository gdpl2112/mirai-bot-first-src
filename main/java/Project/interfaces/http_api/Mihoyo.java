package Project.interfaces.http_api;

import io.github.kloping.mirai0.commons.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import io.github.kloping.mirai0.commons.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import io.github.kloping.MySpringTool.annotations.PathValue;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;

@HttpClient("https://ys.mihoyo.com/")
public interface Mihoyo {
    /**
     * get index news info with id
     *
     * @return
     */
    @GetPath("main/news")
    @Callback("Project.detailPlugin.MihoyoP0.c0")
    MihoyoYuanshen newsIndex();

    /**
     * get detail for id
     *
     * @param id
     * @return
     */
    @GetPath("/main/news/detail/")
    @Callback("Project.detailPlugin.MihoyoP0.c0")
    MihoyoYuanshenDetail newsPoint(@PathValue Object id);
}
