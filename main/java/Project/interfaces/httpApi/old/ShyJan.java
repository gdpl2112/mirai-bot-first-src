package Project.interfaces.httpApi.old;

import Project.commons.apiEntitys.shyJan.SearchResult;
import Project.commons.apiEntitys.shyJan.ShyJanData;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github.kloping
 */
@HttpClient("https://api.shy-jan.xyz/")
public interface ShyJan {
    /**
     * search
     *
     * @param keyword
     * @return
     */
    @GetPath("api/%E5%85%A8%E7%BD%91%E5%BD%B1%E8%A7%86.php")
    SearchResult search(@ParamName("keyword") String keyword);

    /**
     * search
     *
     * @param vid
     * @return
     */
    @GetPath("api/%E5%85%A8%E7%BD%91%E5%BD%B1%E8%A7%86.php")
    @Callback("Project.plugins.All.sjc0")
    ShyJanData get(@ParamName("vid") String vid);
}
