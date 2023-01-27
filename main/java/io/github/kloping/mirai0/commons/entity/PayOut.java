package io.github.kloping.mirai0.commons.entity;

import lombok.Data;

/**
 * <table class="mdui-table mdui-table-hoverable"><thead><tr><th>参数名称</th> <th>参数类型</th> <th>参数说明</th></tr></thead> <tbody><tr><td><code>code</code></td> <td><code>int</code></td> <td>状态码</td></tr><tr><td><code>text</code></td> <td><code>string</code></td> <td>返回提示</td></tr><tr><td><code>data</code></td> <td><code>string</code></td> <td>返回数据</td></tr><tr><td><code>uin</code></td> <td><code>int</code></td> <td>发起订单账号</td></tr><tr><td><code>payuin</code></td> <td><code>int</code></td> <td>付款人账号</td></tr><tr><td><code>Time</code></td> <td><code>string</code></td> <td>付款时间</td></tr><tr><td><code>pay_id</code></td> <td><code>bigint</code></td> <td>订单号</td></tr></tbody></table>
 *
 * @author github.kloping
 */
@Data
public class PayOut {
    private Integer code;
    private String text;
    private PayOutData data;
    private String uin;
    private String payuin;
    private String payid;
    private String time;
}
