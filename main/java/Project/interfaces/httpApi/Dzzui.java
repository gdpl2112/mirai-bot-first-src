package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;

/**
 * @author github.kloping
 */
@HttpClient("https://api.dzzui.com/")
public interface Dzzui {
    /**
     * @return
     */
    @GetPath("api/avatar")
    byte[] avatar();
}
