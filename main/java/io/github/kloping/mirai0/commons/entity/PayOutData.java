package io.github.kloping.mirai0.commons.entity;

import lombok.Data;

/**
 * {"Money":"1.0","pay_uin":"3474006766","Time":"2023-01-27 11:21","uin":"930204019","pay_id":"3023012709302040191394430154"}
 *
 * @author github.kloping
 */
@Data
public class PayOutData {
    private String money;
    private String payUin;
    private String time;
    /**
     * bot id
     */
    private String uin;
    private String payId;
}
