package io.github.kloping;

import Project.commons.SpGroup;

/**
 * @author github.kloping
 */
public interface BotInterface {
    Long getBotId();

    void speak(String line, SpGroup group);
}
