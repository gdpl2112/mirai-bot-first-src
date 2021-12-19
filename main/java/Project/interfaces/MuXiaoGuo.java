package Project.interfaces;

import Entitys.apiEntitys.baiKe.BaiKe;
import Entitys.apiEntitys.sjtx.Sjtx;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

@HttpClient("https://api.muxiaoguo.cn/api/")
public interface MuXiaoGuo {

    /**
     *
     * @param method pc or mobile
     * @return
     */
    @GetPath("sjtx")
    Sjtx getSjtx(@ParamName("method") String method);


    /**
     * @param type Baidu or Sogo
     * @param word
     * @return
     */
    @GetPath("Baike")
    BaiKe getBaiKe(@ParamName("type") String type, @ParamName("word") String word);
}
