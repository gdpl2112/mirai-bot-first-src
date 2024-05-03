package io.github.kloping.kzero.hwxb.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author github.kloping
 */
@Data
public class Source {
    private Contact to;
    private Contact from;
    private Contact room;
}
