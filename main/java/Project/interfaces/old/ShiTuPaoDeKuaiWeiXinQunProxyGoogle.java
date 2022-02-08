package Project.interfaces.old;

import io.github.kloping.MySpringTool.annotations.http.*;
import org.jsoup.nodes.Document;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("https://shitu.paodekuaiweixinqun.com")
public interface ShiTuPaoDeKuaiWeiXinQunProxyGoogle {

    @GetPath("searchbyimage")
    @Callback("Project.detailPlugin.All.shiTuParse")
    public io.github.kloping.mirai0.Entitys.apiEntitys.ShiTu.Response doc(
            @ParamName("image_url") String url,
            @ParamName("encoded_image")
            @DefaultValue("")
                    String encoded_image,
            @ParamName("image_content")
            @DefaultValue("")
                    String image_content,
            @ParamName("filename")
            @DefaultValue("")
                    String filename,
            @ParamName("hl") @DefaultValue("zh-CN")
                    String hl
    );
}
