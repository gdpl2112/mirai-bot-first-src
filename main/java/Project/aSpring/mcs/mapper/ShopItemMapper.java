package Project.aSpring.mcs.mapper;

import Project.aSpring.dao.ShopItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
