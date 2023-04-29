package Project.interfaces.httpApi;

import Project.commons.apiEntitys.qqGroupInfo.QQGroupInfo;
import Project.commons.apiEntitys.qqMemberInfo.QQMemberInfo;
import io.github.kloping.MySpringTool.annotations.http.Callback;
import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;

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
    @Callback("Project.plugins.All.getTalentDays")
    String getTalent(@ParamName("qq") Long qq);
}
