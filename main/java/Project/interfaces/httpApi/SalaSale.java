package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("http://sala.sale/")
public interface SalaSale {
    /**
     * 冬奥排名
     *
     * @return
     */
    @GetPath("api/da.php")
    String winterOlympics();
}
