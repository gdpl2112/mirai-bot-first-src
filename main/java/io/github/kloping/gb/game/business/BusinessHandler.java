package io.github.kloping.gb.game.business;

import io.github.kloping.gb.game.e0.GameDataContext;

/**
 * @author github.kloping
 */
public abstract class BusinessHandler {
    public BusinessHandler next;
    protected StringBuilder sb = new StringBuilder();

    public BusinessHandler(BusinessHandler next) {
        this.next = next;
    }

    /**
     * if return null continue
     *
     * @return
     */
    public String progress(GameDataContext context) {
        BusinessHandler next = getNext();
        if (next != null) {
            return next.progress(context);
        } else return null;
    }

    public BusinessHandler getNext() {
        return next;
    }

    public void setNext(BusinessHandler next) {
        this.next = next;
    }

    public StringBuilder getSb() {
        return sb;
    }
}
