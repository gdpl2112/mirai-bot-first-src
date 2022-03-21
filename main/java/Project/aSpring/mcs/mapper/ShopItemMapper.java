package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.commons.gameEntitys.ShopItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github.kloping
 */
@Repository
public interface ShopItemMapper extends BaseMapper<ShopItem> {
    /**
     * all
     *
     * @return
     */
    @Select("select * from shop_item where state=0;")
    List<ShopItem> all();
}
