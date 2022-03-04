package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface ZongMapper extends BaseMapper<Zong> {
    /**
     * get zong
     *
     * @return
     */
    @Select("SELECT * FROM `zong`")
    List<Zong> selectAll();
}
