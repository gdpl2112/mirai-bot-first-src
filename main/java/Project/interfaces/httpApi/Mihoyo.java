package Project.interfaces.httpApi;

import Project.commons.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import Project.commons.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PathValue;

@HttpClient("https://ys.mihoyo.com/")
public interface Mihoyo {
    /**
     * get index news info with id
     *
     * @return
     */
    @GetPath("main/news")
    @Callback("Project.plugins.MihoyoP0.c0")
    MihoyoYuanshen newsIndex();

    /**
     * get detail for id
     *
     * @param id
     * @return
     */
    @GetPath("/main/news/detail/")
    @Callback("Project.plugins.MihoyoP0.c0")
    MihoyoYuanshenDetail newsPoint(@PathValue Object id);
}
