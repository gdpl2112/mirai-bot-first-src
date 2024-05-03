package io.github.kloping.kzero.hwxb.dto;

import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class FriendOrMember extends User {
    private Boolean friend;
    private Integer gender;
    private Object[] phone;
    private String signature;
    private Boolean star;
    private Integer type;
    private String city;
    private String address;
    private String weixin;
    private String province;
}
