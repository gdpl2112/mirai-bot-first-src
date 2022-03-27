package Project.aSpring.mcs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.kloping.mirai0.commons.UserScore;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author github-kloping
 */
@Repository
public interface UserScoreMapper extends BaseMapper<UserScore> {
    /**
     * 清除所有犯罪
     *
     * @return
     */
    @Update("UPDATE `user_score` SET `fz`=0")
    Integer updateAll();

    /**
     * 清除收益记录
     *
     * @return
     */
    @Update("UPDATE `user_score` SET `earnings`=0,`debuffs`=0")
    Integer updateEarnings();
}
