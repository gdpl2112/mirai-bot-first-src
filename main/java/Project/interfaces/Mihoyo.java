package Project.interfaces;

import Entitys.apiEntitys.mihoyoYuanshen.MihoyoYuanshen;
import Entitys.apiEntitys.mihoyoYuanshenDetail.MihoyoYuanshenDetail;
import io.github.kloping.MySpringTool.annotations.PathValue;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import org.jsoup.nodes.Document;

@HttpClient("https://ys.mihoyo.com/")
public interface Mihoyo {
    /**
     * get index news info with id
     *
     * @return
     */
    @GetPath("main/news")
    @Callback("Project.Plugins.Mihoyo.c0")
    MihoyoYuanshen newsIndex();

    /**
     * get detail for id
     *
     * @param id
     * @return
     */
    @GetPath("/main/news/detail/")
    @Callback("Project.Plugins.Mihoyo.c0")
    MihoyoYuanshenDetail newsPoint(@PathValue Object id);
}
