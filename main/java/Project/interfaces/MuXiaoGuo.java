package Project.interfaces;

import Entitys.apiEntitys.sjtx.Sjtx;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

@HttpClient("https://api.muxiaoguo.cn/api/")
public interface MuXiaoGuo {
    //https://api.muxiaoguo.cn/api/sjtx?method=pc

    /**
     *
     * @param method pc or mobile
     * @return
     */
    @GetPath("sjtx")
    Sjtx getSjtx(@ParamName("method") String method);
}
