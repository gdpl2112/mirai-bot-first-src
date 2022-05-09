package io.github.kloping.mirai0.Main.temp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static io.github.kloping.mirai0.Main.temp.C1.getTimeFromNowTo;

/**
 * @author github.kloping
 */
@Data
@Setter
@Getter
@Accessors(chain = true)
@EqualsAndHashCode
public class C0 {
    private int hour;
    private int minutes;
    private String content;
    private long targetId;
    private String type;

    public long st() {
        long t0 = getTimeFromNowTo(getHour(), getMinutes(), 0);
        return t0 <= 0 ? t0 + 1000 * 60 * 60 * 24 : t0;
    }
}
