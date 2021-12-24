package Project.interfaces;

import io.github.kloping.MySpringTool.annotations.PathValue;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import org.jsoup.nodes.Document;

@HttpClient("https://ys.mihoyo.com/")
public interface Mihoyo {
    @GetPath("main/news")
    Document news_index();

    @GetPath("/main/news/detail/")
    Document news_point(@PathValue Object id);
}
