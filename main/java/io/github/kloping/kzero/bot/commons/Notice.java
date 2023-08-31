package io.github.kzero.bot.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author github-kloping
 * @date 2023-03-16
 */
@Getter
@Setter
@Accessors(chain = true)
public class Notice {
    private Long id;
    private Integer state;
    private Integer views;
    private String title;
    private String icon;
    private String date;
    private String html;
    private Long time;
    private String authorName;
    private Long authorId;
}
