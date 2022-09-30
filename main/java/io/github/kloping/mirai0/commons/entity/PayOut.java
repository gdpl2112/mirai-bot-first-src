package io.github.kloping.mirai0.commons.entity;

import lombok.Data;

/**
 * <table class="layui-table" lay-size="sm">
 * <thead>
 * <tr><th>名称</th><th>类型</th><th>说明</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>code</td><td>Int</td><td>状态码</td></tr>
 * <tr><td>text</td><td>String</td><td>返回提示</td></tr>
 * <tr><td>money</td><td>Int</td><td>发起金额</td></tr>
 * <tr><td>uin</td><td>Int</td><td>操作者QQ</td></tr>
 * <tr><td>payuin</td><td>Int</td><td>付款者QQ</td></tr>
 * <tr><td>payid</td><td>Int</td><td>订单号</td></tr>
 * </tbody>
 * </table>
 *
 * @author github.kloping
 */
@Data
public class PayOut {
    private Integer code;
    private String text;
    private String money;
    private String uin;
    private String pay_uin;
    private String pay_id;
}
