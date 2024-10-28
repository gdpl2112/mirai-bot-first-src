package io.github.kloping.kzero.hwxb.controller;

import io.github.kloping.kzero.hwxb.event.MetaEvent;

/**
 * @author github.kloping
 */
public interface WxBotEventRec {
    Object rec(MetaEvent event);
}
