package io.github.kloping.kzero.bot.commons.apis.baiduShitu.response;

public class ShituData {
    private String fromUrl;
    private Number width;
    private String objUrl;
    private Number index;
    private String contsign;
    private String thumbUrl;
    private Number height;

    public String getFromUrl() {
        return this.fromUrl;
    }

    public ShituData setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
        return this;
    }

    public Number getWidth() {
        return this.width;
    }

    public ShituData setWidth(Number width) {
        this.width = width;
        return this;
    }

    public String getObjUrl() {
        return this.objUrl;
    }

    public ShituData setObjUrl(String objUrl) {
        this.objUrl = objUrl;
        return this;
    }

    public Number getIndex() {
        return this.index;
    }

    public ShituData setIndex(Number index) {
        this.index = index;
        return this;
    }

    public String getContsign() {
        return this.contsign;
    }

    public ShituData setContsign(String contsign) {
        this.contsign = contsign;
        return this;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public ShituData setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public Number getHeight() {
        return this.height;
    }

    public ShituData setHeight(Number height) {
        this.height = height;
        return this;
    }
}