package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import Project.commons.gameEntitys.Zon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author github.kloping
 */
@Mapper
public interface ZonMapper extends BaseMapper<Zon> {
    /**
     * get zon list
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM `zon` WHERE `id`=#{id}")
    List<Zon> selectByZongId(@Param("id") Integer id);

    /**
     * get zon list
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM `zon` WHERE `id`=#{id} and `level`>0")
    List<Zon> selectEldersByZongId(@Param("id") Integer id);

    /***
     * 更新
     * @return
     */
    @Update("UPDATE `zon` SET `active`=0;")
    int updateAll();
}
