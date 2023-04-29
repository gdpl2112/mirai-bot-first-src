package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.entity.PayOut;

/**
 * @author github.kloping
 */
@HttpClient("https://xiaobapi.top/")
public interface XiaoBapi {
    /**
     * 参数：
     * qq：需要付钱的QQ(必填)
     * uin：操作者QQ(必填)
     * group：群号(必填)
     * pskey：操作者QQ的pskey(必填)
     * title：群收款说明(必填)
     * jie：收费价格(必填)
     * select：1(必填)
     *
     * @param title
     * @param Skey
     * @param pskey
     * @param money
     * @param group
     * @param qq
     * @param uin
     * @param payid
     * @param select
     * @return
     */
    @GetPath("api/xb/api/pay.php")
    PayOut pay(@ParamName("title") String title,
               @ParamName("pskey") String pskey,
               @ParamName("jie") Float money,
               @ParamName("group") Long group,
               @ParamName("qq") Long qq,
               @ParamName("uin") Long uin,
               @ParamName("payid") String payid,
               @ParamName("select") Integer select);
}
