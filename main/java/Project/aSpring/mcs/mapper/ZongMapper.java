package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import Project.aSpring.dao.Zong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * get zong sort
     *
     * @return
     */
    @Select("SELECT * FROM `zong` ORDER BY `active` DESC")
    List<Zong> selectAllSortByActive();

    /**
     * 更新
     *
     * @return
     */
    @Update("UPDATE `zong` SET `active`=0;")
    int updateAll();
}
