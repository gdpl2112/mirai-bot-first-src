package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.Entitys.apiEntitys.qqGroupInfo.QQGroupInfo;
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

    /**
     * get group
     *
     * @param qq
     * @return
     */
    @GetPath("api/groupinformation/")
    QQGroupInfo getGroupInfo(@ParamName("qh") Long qq);

    /**
     * qq达人
     *
     * @param qq
     * @return
     */
    @GetPath("api/talent/")
    @Callback("Project.detailPlugin.All.getTalentDays")
    String getTalent(@ParamName("qq") Long qq);
}
