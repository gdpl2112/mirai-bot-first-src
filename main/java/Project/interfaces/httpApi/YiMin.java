package Project.interfaces.httpApi;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.http.DefaultValue;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

/**
 * @author github.kloping
 */
@HttpClient("https://api.wer.plus/")
public interface YiMin {
    /**
     * @param url
     * @param name
     * @param rerd
     * @param cmve
     * @param cpgt
     * @param maim
     * @return
     */
    @GetPath("api/qian")
    JSONObject qian(
            @ParamName("url")
            String url,
            @ParamName("name")
            String name,
            @ParamName("rerd")
            @DefaultValue("奖励内容")
            String rerd,
            @ParamName("cmve")
            @DefaultValue("累计内容")
            String cmve,
            @ParamName("cpgt")
            @DefaultValue("下方版权内容")
            String cpgt,
            @ParamName("maim")
            @DefaultValue("附加内容")
            String maim
    );
}
