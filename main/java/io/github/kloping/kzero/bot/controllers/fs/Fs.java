package io.github.kloping.kzero.bot.controllers.fs;

import java.text.SimpleDateFormat;

/**
 * @author github.kloping
 */
public interface Fs {

    SimpleDateFormat SF_MM = new SimpleDateFormat("mm");

    String getName();

    Integer ftype();
}
