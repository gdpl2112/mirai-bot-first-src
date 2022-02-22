package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("http://qxu66.top")
public interface Qxu66 {
    /**
     * 举牌子
     *
     * @param msg
     * @return
     */
    @GetPath("/api/zt.php")
    byte[] jupaizi(@ParamName("msg") String msg);
}
