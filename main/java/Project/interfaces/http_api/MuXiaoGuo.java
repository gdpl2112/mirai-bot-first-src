package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.apiEntitys.baiKe.BaiKe;
import io.github.kloping.mirai0.commons.apiEntitys.reping163.Reping163;
import io.github.kloping.mirai0.commons.apiEntitys.sjtx.Sjtx;

@HttpClient("https://api.muxiaoguo.cn/api/")
public interface MuXiaoGuo {

    /**
     * @param method pc or mobile
     * @return
     */
    @GetPath("sjtx")
    Sjtx getSjtx(@ParamName("method") String method);


    /**
     * get bk
     *
     * @param type Baidu or Sogo
     * @param word
     * @return
     */
    @GetPath("Baike")
    BaiKe getBaiKe(@ParamName("type") String type, @ParamName("word") String word);

    /**
     * 获取  163 网易一个 热评
     *
     * @return
     */
    @GetPath("163reping")
    Reping163 reping();
}
