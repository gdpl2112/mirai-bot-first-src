package io.github.kloping.kzero.bot.commons.apiEntitys.baidu;


import com.alibaba.fastjson.JSON;
import io.github.kloping.MySpringTool.entity.KeyVals;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author github.kloping
 */
public class AiRequest implements KeyVals {
    private String type = "tns";
    private Integer per = 4103;
    private Integer spd = 5;
    private Integer pit = 5;
    private Integer vol = 5;
    private Integer aue = 6;
    private String tex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

    public Integer getSpd() {
        return spd;
    }

    public void setSpd(Integer spd) {
        this.spd = spd;
    }

    public Integer getPit() {
        return pit;
    }

    public void setPit(Integer pit) {
        this.pit = pit;
    }

    public Integer getVol() {
        return vol;
    }

    public void setVol(Integer vol) {
        this.vol = vol;
    }

    public Integer getAue() {
        return aue;
    }

    public void setAue(Integer aue) {
        this.aue = aue;
    }

    public String getTex() {
        return tex;
    }

    public void setTex(String tex) {
        this.tex = tex;
    }

    @Override
    public Collection<HttpConnection.KeyVal> values() {
        List<HttpConnection.KeyVal> list = new ArrayList<>();
        JSON.parseObject(JSON.toJSONString(this)).forEach((k, v) -> {
            list.add(HttpConnection.KeyVal.create(k, v.toString()));
        });
        return list;
    }
}
