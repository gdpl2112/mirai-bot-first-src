package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github.kloping
 */
@HttpClient("http://api2.888655.xyz/")
public interface Api2888655 {
    @GetPath("api/extract.php")
    Object o(@ParamName("url") String url);
}
