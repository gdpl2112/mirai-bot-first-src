package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("http://api.weijieyue.cn/")
public interface WeiJieYue {
    /**
     * 爬
     *
     * @param qq
     * @return
     */
    @GetPath("api/tupian/pa.php")
    byte[] paImg(@ParamName("qq") Long qq);

    /**
     * 赞
     *
     * @param qq
     * @return
     */
    @GetPath("api/tupian/zan.php")
    byte[] zan(@ParamName("qq") Long qq);
}
