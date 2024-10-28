package io.github.kloping.kzero.hwxb.controller;

import io.github.kloping.kzero.hwxb.event.MetaEvent;

/**
 * @author github.kloping
 */
public interface WxBotEventRecv {
    Object rec(MetaEvent event);
}
