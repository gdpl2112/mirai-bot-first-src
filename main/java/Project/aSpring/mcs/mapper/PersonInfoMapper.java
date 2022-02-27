package Project.aSpring.mcs.mapper;

import io.github.kloping.mirai0.Entitys.gameEntitys.PersonInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author github-kloping
 */
@Repository
public interface PersonInfoMapper extends BaseMapper<PersonInfo> {
    /**
     * get排名
     *
     * @param num
     * @return
     */
    @Select("SELECT * FROM person_info ORDER BY level DESC LIMIT #{num}")
    List<PersonInfo> getOrderByLevel(@Param("num") Integer num);

    /**
     * 更新全部 0点
     *
     * @return
     */
    @Update("UPDATE `person_info` SET `tag`='',`help_c`=0,`help_toc`=0,`buy_help_c`=0,`died`=0,`downed`=0,`buy_help_c`=0,`buy_help_to_c`=0")
    Integer updateAll();
}
