package io.github.kloping.mirai0.commons.entity;


import lombok.Data;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author HRS-Computer
 */
@Data
public class PayOutM {
    private PayOut out;
    private Long time;
    private Long gid;
    private Long qid;
    private Long bid;
    private Integer value;
}
