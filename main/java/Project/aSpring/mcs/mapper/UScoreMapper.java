package Project.aSpring.mcs.mapper;

import io.github.kloping.mirai0.Entitys.UScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UScoreMapper extends BaseMapper<UScore> {
    @Select("select * from u_score")
    List<UScore> gets();
}
