package io.github.kloping.kzero.hwxb.dto;

import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class PayLoad {
    private String id;
    private String alias;
    private String avatar;
    private Boolean friend;
    private Integer gender;
    private String name;
    private Object[] phone;
    private String signature;
    private Boolean star;
    private Integer type;
}
