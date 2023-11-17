package io.github.kloping.kzero.bot.commons.apis;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class BottleMessage {
    private Integer id;
    private String gid;
    private String sid;
    private Long time;
    private String name;
    private String message;
    private Integer state;
}
