package io.github.kloping.mirai0.Entitys.gameEntitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static Project.dataBases.GameDataBase.getWarp;
import static Project.dataBases.GameDataBase.setWarp;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Warp {
    private Number id = -1;
    private Number bindQ = -1;
    private Number master = -1;
    private Number prentice = -1;

    public Warp setId(String id) {
        try {
            this.id = Long.parseLong(id.trim());
        } catch (Exception e) {
        }
        return this;
    }

    public static Warp getInstance(long q) {
        return getWarp(q);
    }

    public void apply() {
        setWarp(this);
    }
}
