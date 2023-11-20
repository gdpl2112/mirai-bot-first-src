package io.github.kloping.kzero.game.commons;

import io.github.kloping.kzero.spring.dao.PersonInfo;
import io.github.kloping.kzero.spring.dao.WhInfo;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Getter
@Data
@Accessors(chain = true)
public class GameUserInfo {
    private PersonInfo personInfo;
    private WhInfo whInfo;

    public GameUserInfo(PersonInfo personInfo, WhInfo whInfo) {
        this.personInfo = personInfo;
        this.whInfo = whInfo;
    }

}
