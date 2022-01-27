package io.github.kloping.mirai0.Entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupConf {
    private long id;
    private boolean open = true;
    private boolean speak = true;
    private boolean show = true;
    private boolean cap = true;
}