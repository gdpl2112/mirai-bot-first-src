package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.*;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.BaiduShitu;
import io.github.kloping.mirai0.Entitys.apiEntitys.baiduShitu.response.BaiduShituResponse;

import java.util.Map;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("https://graph.baidu.com/")
public interface IBaiduShitu {
    /**
     * 百度Shi图结果
     *
     * @param sign
     * @return
     */
    @GetPath("ajax/pcsimi")
    BaiduShituResponse response(@ParamName("sign") String sign);

    /**
     * @param headers
     * @param body
     * @param tn
     * @param from
     * @param source
     * @param range
     * @param imageUrl
     * @param time
     * @return
     */
    @PostPath("upload")
    BaiduShitu get(
            @Headers
                    Map<String, String> headers,
            @RequestBody(type = RequestBody.type.toString)
                    String body,
            @ParamName("tn")
            @DefaultValue("pc")
                    String tn,
            @ParamName("from")
            @DefaultValue("pc")
                    String from,
            @ParamName("image_source")
            @DefaultValue("PC_UPLOAD_URL")
                    String source,
            @ParamName("range")
            @DefaultValue("=%7B%22page_from%22:%20%22shituIndex%22%7D")
                    String range,
            @ParamName("image")
                    String imageUrl,
            @ParamName("uptime")
                    Long time
    );
}
