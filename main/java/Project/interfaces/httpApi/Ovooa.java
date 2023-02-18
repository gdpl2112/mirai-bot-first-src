package Project.interfaces.httpApi;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.entity.PayOut;

/**
 * @author github.kloping
 */
@HttpClient("https://ovooa.com/")
public interface Ovooa {
    /**
     * pa
     *
     * @param qq
     * @return
     */
    @GetPath("API/pa/api.php")
    byte[] pa(@ParamName("QQ") Long qq);

    /**
     * <table class="mdui-table mdui-table-hoverable"><thead><tr><th>参数名称</th> <th>参数类型</th> <th>是否必填</th> <th>备注内容</th></tr></thead> <tbody><tr><td><code>uin</code></td> <td><code>int</code></td> <td><code>是</code></td> <td>付款对象</td></tr><tr><td><code>QQ</code></td> <td><code>int</code></td> <td><code>是</code></td> <td>提供Skey的账号</td></tr><tr><td><code>Group</code></td> <td><code>int</code></td> <td><code>是</code></td> <td>发起收款的群号</td></tr><tr><td><code>Select</code></td> <td><code>int</code></td> <td><code>是</code></td> <td>类型，1为发起，2为查看，3为取消订单</td></tr><tr><td><code>payid</code></td> <td><code>bigint</code></td> <td><code>否</code></td> <td>订单号，查询或取消必填</td></tr><tr><td><code>Money</code></td> <td><code>string</code></td> <td><code>是</code></td> <td>金额，发起必填</td></tr><tr><td><code>Title</code></td> <td><code>string</code></td> <td><code>否</code></td> <td>收款标题</td></tr></tbody></table>
     *
     * @param title
     * @param pskey
     * @param je
     * @param group
     * @param qq
     * @param uin
     * @param payid
     * @param select
     * @return
     */
    @GetPath("/API/Group_pay/api")
    PayOut pay(@ParamName("Title") String title,
               @ParamName("Skey") String Skey,
               @ParamName("pskey") String pskey,
               @ParamName("Money") Float money,
               @ParamName("Group") Long group,
               @ParamName("QQ") Long qq,
               @ParamName("uin") Long uin,
               @ParamName("payid") String payid,
               @ParamName("Select") Integer select);
}
