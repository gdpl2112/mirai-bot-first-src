package Project.controllers;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.spt.RedisOperate;

import java.util.List;
import java.util.Map;

/**
 * @author github-kloping
 */
@Controller
public class FirstController {
    @AutoStand
    public RedisOperate<Map<Long, Long>> redisOperate;
}
