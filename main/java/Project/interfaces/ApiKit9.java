package Project.interfaces;

import io.github.kloping.MySpringTool.annotations.Param;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.Entitys.apiEntitys.qqMemberInfo.QQMemberInfo;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("https://api.kit9.cn/")
public interface ApiKit9 {
    /**
     * get info
     *
     * @param qq
     * @return
     */
    @GetPath("api/member/")
    QQMemberInfo getInfo(@ParamName("qq") Long qq);
}
