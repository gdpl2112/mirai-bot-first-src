package io.github.kloping.gb.game.e0;

import io.github.kloping.gb.spring.dao.PersonInfo;
import io.github.kloping.gb.spring.dao.WhInfo;

/**
 * @author github-kloping
 * @date 2023-07-04
 */
public class GameDataContext {
    private String id;
    private WhInfo whInfo;
    private PersonInfo personInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameDataContext(String id, WhInfo whInfo, PersonInfo personInfo) {
        this.id = id;
        this.whInfo = whInfo;
        this.personInfo = personInfo;
    }

    public WhInfo getWhInfo() {
        return whInfo;
    }

    public void setWhInfo(WhInfo whInfo) {
        this.whInfo = whInfo;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
