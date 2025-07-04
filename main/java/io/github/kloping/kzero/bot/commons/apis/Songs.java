package io.github.kloping.kzero.bot.commons.apis;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Songs {
    private int state;
    private int num;
    private long time;
    private String keyword;
    private Song[] data;
    private String type;
}
