package Project.interfaces.httpApi;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.http.*;
import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * @author github.kloping
 */
@HttpClient("https://user.qzone.qq.com/")
public interface QZone {
    /**
     * main
     *
     * @param qid
     * @param uin
     * @param puin
     * @param pskey
     * @return
     */
    @GetPath("{qid}/main")
    Document main(@PathValue("qid") Long qid,
                  @CookieValue Map.Entry<String, String> uin,
                  @CookieValue Map.Entry<String, String> puin,
                  @CookieValue Map.Entry<String, String> pskey
    );

    /**
     * cgi
     *
     * @param qid
     * @param gtk
     * @param param
     * @param uin
     * @param puin
     * @param pskey
     * @return
     */
    @Callback("Project.plugins.All.sub")
    @GetPath("proxy/domain/r.qzone.qq.com/cgi-bin/main_page_cgi")
    JSONObject mainCgi(
            @ParamName("uin") Long qid,
            @ParamName("g_tk") @DefaultValue("996183676") String gtk,
            @ParamName("param") String param,
            @CookieValue Map.Entry<String, String> uin,
            @CookieValue Map.Entry<String, String> puin,
            @CookieValue Map.Entry<String, String> pskey
    );

    @GetPath("/proxy/domain/ic2.qzone.qq.com/cgi-bin/feeds/feeds_html_module")
    Document feedds(
            @ParamName("i_uin") Long qid,
            @ParamName("i_login_uin") @DefaultValue("930204019") Long iqid,
            @ParamName("showcount") @DefaultValue("5") Integer count, @CookieValue Map.Entry<String, String> uin,
            @CookieValue Map.Entry<String, String> puin,
            @CookieValue Map.Entry<String, String> pskey);
}
