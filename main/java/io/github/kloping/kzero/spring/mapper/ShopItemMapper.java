package io.github.kzero.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kzero.spring.dao.ShopItem;
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
