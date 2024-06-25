package io.github.kloping.kzero.mirai;

import io.github.kloping.spt.annotations.ComponentScan;

/**
 * @author github.kloping
 */
@ComponentScan(value = "io.github.kloping.kzero.bot", path = {
        "io.github.kloping.kzero.mirai.exclusive"
        , "io.github.kloping.kzero.mirai.listeners"
})
public class MiraiStartClass {

}
