package io.github.kloping.mirai0.commons.game;

import io.github.kloping.MySpringTool.annotations.Action;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author github.kloping
 */
@Setter
@Getter
@Accessors(chain = true)
@EqualsAndHashCode
public class ChallengeSide {
    private Long qid;
    private int x, y;
    private String side;
    private String icon;
    private int maxMove = 2;
    private boolean operation = false;

    public String getIcon() {
        icon = "https://q1.qlogo.cn/g?b=qq&nk=" + qid + "&s=640";
        return icon;
    }
}
