package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.PostPath;
import io.github.kloping.MySpringTool.annotations.http.RequestData;

import java.util.Map;

/**
 * @author github.kloping
 */
@HttpClient("http://www.atoolbox.net/")
public interface Atoolbox {
    /**
     * 生成举牌子
     *
     * @param c 内容
     * @param t 透明 Boolean
     * @param b 背景颜色
     * @return
     */
    @PostPath("Api/GetHoldUpSignImage.php")
    String s0(@RequestData Map.Entry<String, String> c,
              @RequestData Map.Entry<String, Boolean> t,
              @RequestData Map.Entry<String, String> b);
}
