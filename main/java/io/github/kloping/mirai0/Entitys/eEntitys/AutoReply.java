package io.github.kloping.mirai0.Entitys.eEntitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AutoReply {
    private Integer id;
    private String who;
    private String k;
    private String v;
    private String time;
    private int deleteStat;
}
