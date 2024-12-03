package io.github.kloping.kzero.spring.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单词
 *
 * @author github kloping
 * @date 2024/12/3-13:47
 */
@Data
@Accessors(chain = true)
public class Vocabulary {
    @TableId(type = IdType.NONE)
    private String word;
    private String mean;

    private String timestamp;
}
