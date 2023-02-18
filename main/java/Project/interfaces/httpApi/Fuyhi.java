package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github.kloping
 */
@HttpClient("http://fuyhi.top/")
public interface Fuyhi {
    @GetPath("api/qqcye/api.php")
    String yuE(
            @ParamName("qq") Long qq,
            @ParamName("skey") String skey,
            @ParamName("pskey") String pskey);
}
