package Project.detailPlugin;

import Entitys.apiEntitys.pvpQQH0.Data;
import Entitys.apiEntitys.pvpQQH0.PvpQQH0;
import Entitys.apiEntitys.pvpQQVoice.PvpQQVoice;
import Entitys.apiEntitys.pvpQQVoice.Yy_4e;
import Entitys.apiEntitys.pvpQQVoice.Yylb_34;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.initialize.FileInitializeValue;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author github-kloping
 * @version 1.0
 * @date 2021/12/30-11
 */
@Entity
public class PvpQq {
    public static String c1(String arg) {
        int i1 = arg.indexOf("(");
        int i2 = arg.lastIndexOf(")");
        return arg.substring(i1 + 1, i2);
    }

    public static String c0(String arg) {
        int i1 = arg.indexOf("(");
        int i2 = arg.lastIndexOf(")");
        return arg.substring(i1 + 1, i2).replaceAll("故事站-英雄列表-", "");
    }

    public static final Map<String, Yy_4e[]> NAME2VOICE = new ConcurrentHashMap<>();
    public static final Map<String, Entitys.apiEntitys.pvpQQH0.Data> NAME2DATA = new ConcurrentHashMap<>();
    public static final Map<String, Integer> NAME2ID = new ConcurrentHashMap<>();
    public static final Map<Integer, String> ID2NAME = new ConcurrentHashMap<>();

    @AutoStand
    Project.interfaces.PvpQq pvpQq;

    public void m1() {
        PvpQQH0 g = pvpQq.get1("createHeroList");
        PvpQQVoice v = pvpQq.get0("createList");
        FileInitializeValue.putValues("./data/pvpqq/hd.json", g, true);
        FileInitializeValue.putValues("./data/pvpqq/vs.json", v, true);
    }

    public void m0() {
        if (!new File("./data/pvpqq/hd.json").exists()) {
            m1();
        }
        PvpQQH0 g = new PvpQQH0();
        PvpQQVoice v = new PvpQQVoice();
        g = FileInitializeValue.getValue("./data/pvpqq/hd.json", g, true);
        v = FileInitializeValue.getValue("./data/pvpqq/vs.json", v, true);
        for (Data datum : g.getData()) {
            if (datum == null) continue;
            String name = datum.getTitle().replace("故事站-英雄列表-", "");
            int id = Integer.valueOf(datum.getHeroid()).intValue();
            NAME2ID.put(name, id);
            ID2NAME.put(id, name);
            NAME2DATA.put(name, datum);
        }
        for (Yylb_34 yylb_34 : v.getYylb_34()) {
            int id = Integer.parseInt(yylb_34.getYxid_a7());
            NAME2VOICE.put(ID2NAME.get(id), yylb_34.getYy_4e());
        }
    }

    public Yy_4e[] getY4e(String name) {
        if (NAME2VOICE.isEmpty()) {
            m0();
        }
        if (NAME2VOICE.containsKey(name)) {
            return NAME2VOICE.get(name);
        } else {
            return null;
        }
    }

    public Entitys.apiEntitys.pvpQQH0.Data getD(String name) {
        if (NAME2DATA.isEmpty()) {
            m0();
        }
        if (NAME2DATA.containsKey(name)) {
            return NAME2DATA.get(name);
        } else {
            return null;
        }
    }
}
