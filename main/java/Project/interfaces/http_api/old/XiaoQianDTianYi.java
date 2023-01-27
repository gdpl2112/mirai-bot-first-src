package Project.interfaces.http_api.old;

import io.github.kloping.MySpringTool.annotations.http.GetPath;
import io.github.kloping.MySpringTool.annotations.http.HttpClient;
import io.github.kloping.MySpringTool.annotations.http.ParamName;
import io.github.kloping.mirai0.commons.entity.PayOut;

/**
 * @author github.kloping
 */
@HttpClient("http://www.xiaoqiandtianyi.tk")
public interface XiaoQianDTianYi {
    /**
     * <table class="layui-table" lay-size="sm">
     * <thead>
     * <tr><th>名称</th><th>类型</th><th>必填</th><th>说明</th></tr>
     * </thead>
     * <tbody>
     * <tr><td>title</td><td>String</td><td>是</td><td>发起收款的标题</td></tr>
     * <tr><td>type</td><td>String</td><td>否</td><td>返回格式，默认json，可选text</td></tr>
     * <tr><td>pskey</td><td>String</td><td>是</td><td>tenpay.com的pskey</td></tr>
     * <tr><td>je</td><td>Int</td><td>是</td><td>金额，最大2000，最小0.01</td></tr>
     * <tr><td>group</td><td>Int</td><td>是</td><td>群号</td></tr>
     * <tr><td>qq</td><td>Int</td><td>是</td><td>付款对象</td></tr>
     * <tr><td>uin</td><td>Int</td><td>是</td><td>操作者账号</td></tr>
     * <tr><td>payid</td><td>Int</td><td>是</td><td>订单号，查询，取消，催收时必须</td></tr>
     * <tr><td>select</td><td>Int</td><td>是</td><td>类型，1为发起，2为查询，3为收，4为取消</td></tr>
     * </tbody>
     * </table>
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
    @GetPath("api/pay.php")
    PayOut pay(
            @ParamName("title") String title,
            @ParamName("pskey") String pskey,
            @ParamName("je") Float je,
            @ParamName("group") Long group,
            @ParamName("qq") Long qq,
            @ParamName("uin") Long uin,
            @ParamName("payid") String payid,
            @ParamName("select") Integer select);

}
