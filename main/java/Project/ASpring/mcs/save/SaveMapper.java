package Project.ASpring.mcs.save;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.Entitys.gameEntitys.GInfo;
import io.github.kloping.mirai0.Main.Handlers.AllMessage;
import org.springframework.stereotype.Repository;

/**
 * @author github kloping
 * @version 1.0
 */
@Repository
public interface SaveMapper extends BaseMapper<AllMessage> {
}
