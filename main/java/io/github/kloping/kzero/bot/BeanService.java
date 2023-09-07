package io.github.kloping.kzero.bot;

import io.github.kloping.MySpringTool.annotations.Bean;
import io.github.kloping.MySpringTool.annotations.Entity;

/**
 * @author github.kloping
 */
@Entity
public class BeanService {
    @Bean("super_id")
    public String id() {
        return "3474006766";
    }
}
