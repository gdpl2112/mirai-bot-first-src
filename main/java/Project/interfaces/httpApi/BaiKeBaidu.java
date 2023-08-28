package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PathValue;
import org.jsoup.nodes.Document;

/**
 * @author github.kloping
 */
@HttpClient("https://baike.baidu.com")
public interface BaiKeBaidu {
    /**
     * 百科
     *
     * @param name
     * @return
     */
    @GetPath("/item/")
    Document doc(@PathValue String name);
}
