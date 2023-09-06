package io.github.kloping.kzero.bot.commons.apis.magiconch;

/**
 * @author github.kloping
 */
public class MagiconchNbnhhshRequest {
    private String text = "";

    public MagiconchNbnhhshRequest(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{\"text\":\"" + text + "\"}";
    }
}
