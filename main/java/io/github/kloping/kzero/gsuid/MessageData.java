package io.github.kloping.kzero.gsuid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author github-kloping
 * @date 2023-06-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String type;
    private Object data;
}
