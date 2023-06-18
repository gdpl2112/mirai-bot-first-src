package Project.services.impl;

import Project.aSpring.SpringBootResource;
import Project.commons.broadcast.enums.ObjType;
import Project.dataBases.SourceDataBase;
import Project.interfaces.Iservice.IGameWeaService;
import Project.services.detailServices.GameWeaDetailService;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.commons.rt.ResourceSet.FinalString.*;
import static Project.dataBases.GameDataBase.*;
import static Project.services.detailServices.GameWeaDetailService.MAX_DAMAGE;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameWeaServiceImpl implements IGameWeaService {

    private static final Pattern PATTERN = Pattern.compile("(\\[@\\d+]|#)");

    @AutoStand
    public static GameWeaDetailService gameWeaDetailService;
    public static String MENU = "";

    private static void init() {
        MENU += "\n#选择器(#为当前魂兽))";
        MENU += "\n使用暗器<暗器名><选择器>";
        MENU += "\n武器背包";
        MENU += "\n制作暗器<暗器名>";
        MENU += "\n暗器制作表";
        MENU += "\n分解<暗器名>";
        MENU += "\n##最大伤害";
        MAX_DAMAGE.forEach((k, v) -> {
            MENU += ("\n\t" + getNameById(k) + "=>" + v);
        });
    }

    public static List<String> getLps(String ss) {
        List<String> list = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(ss);
        while (matcher.find()) {
            list.add(matcher.group().trim());
        }
        return list;
    }

    @Override
    public String useAq(String str, Long who) {
        List<String> list = getLps(str);
        for (int i = list.size() - 1; i > -1; i--) {
            String s1 = list.get(i);
            str = str.replace(s1, "");
            list.remove(s1);
            list.add(s1.replace("[@", "").replace("]", ""));
        }
        return gameWeaDetailService.useAq(list, who, str.trim());
    }

    @Override
    public String aqBgs(Long who) {
        List<String> list = new ArrayList<>();
        Map<Integer, Map.Entry<Integer, Integer>> maps = getBgsw(who);
        Map<Integer, Integer> oid2num = new LinkedHashMap<>();
        if (maps.isEmpty()) {
            return "你没有暗器!";
        }
        for (Integer i : maps.keySet()) {
            int oid = maps.get(i).getKey();
            int num = maps.get(i).getValue();
            if (oid2num.containsKey(oid)) {
                oid2num.put(oid,
                        oid2num.get(oid) + num);
            } else {
                oid2num.put(oid, num);
            }
        }
        AtomicReference<Integer> i = new AtomicReference<>(1);
        oid2num.forEach((k, v) -> {
            list.add(i.getAndSet(i.get() + 1) + "=>" + getNameById(k) + ":剩余" + v + "次");
        });
        return getImageFromStrings(list.toArray(new String[0]));
    }

    @Override
    public String makeAq(Long who, int id) {
        int num = getNumFromBgs(who, 1000);
        int ns = ID_2_WEA_MAPS.get(id);
        if (num >= ns) {
            addToAqBgs(who, id, (ID_2_WEA_O_NUM_MAPS.get(id)));
            for (int i = 0; i < ns; i++) {
                removeFromBgs(who, 1000, ObjType.use);
            }
            return "制作成功" + SourceDataBase.getImgPathById(id) + "\r\n" + ID_2_INTRO_MAPS.get(id);
        } else {
            return "\"" + getNameById(id) + "\"制作的暗器零件不足,需要" + ns + "个";
        }
    }

    @Override
    public String aqMeun() {
        if (MENU == null || MENU.isEmpty())
            init();
        return MENU.trim();
    }

    @Override
    public String aqList() {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Integer i : ID_2_WEA_MAPS.keySet()) {
            if (i < 1000) continue;
            list.add("\"" + getNameById(i) + "\"需要" + ID_2_WEA_MAPS.get(i) + "个零件");
            sb.append("\"" + getNameById(i) + "\"需要" + ID_2_WEA_MAPS.get(i) + "个零件").append("\r\n");
        }
        return getImageFromStrings(list.toArray(new String[0])) + "\r\n" + sb;
    }

    @Override
    public String decomposition(long qid, Integer id, Integer nc) {
        if (id > 1000) {
            //源使用次数
            int numc = ID_2_WEA_O_NUM_MAPS.get(id);
            //需要数量
            int num = ID_2_WEA_MAPS.get(id);
            //每次的数量
            int n = num / numc;
            for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(qid)) {
                int oid = map.get("oid");
                if (oid == id) {
                    int sl = map.get("num");
                    int e0 = sl * n;
                    e0 = (int) (e0 * 0.9f);
                    SpringBootResource.getAqBagMapper().delete(map.get("id"));
                    addToBgs(qid, 1000, e0, ObjType.un);
                    return "成功分解了" + getNameById(id) + "获得了" + e0 + "个暗器零件";
                }
            }
            return NOT_FOUND_THIS_AQ_IN_BG_TIPS;
        } else if (id == 120) {
            StringBuilder sb = new StringBuilder();
            for (Integer i = 0; i < nc; i++) {
                if (!containsInBg(id, qid)) sb.append("你的背包里没有" + getNameById(id));
                removeFromBgs(qid, id, 1, ObjType.un);
                int r = Tool.INSTANCE.RANDOM.nextInt(10);
                int oid;
                if (r == 0) {
                    oid = 122;
                    addToBgs(qid, oid, 1, ObjType.un);
                    sb.append("成功分解了" + getNameById(id) + "获得了1个" + ID_2_NAME_MAPS.get(oid));
                } else if (r == 1 || r == 2) {
                    oid = 121;
                    addToBgs(qid, oid, 2, ObjType.un);
                    sb.append("成功分解了" + getNameById(id) + "获得了2个" + ID_2_NAME_MAPS.get(oid));
                } else {
                    oid = 121;
                    addToBgs(qid, oid, 1, ObjType.un);
                    sb.append("成功分解了" + getNameById(id) + "获得了1个" + ID_2_NAME_MAPS.get(oid));
                }
                sb.append(NEWLINE);
            }
            return sb.toString().trim();
        }
        return ERR_TIPS;
    }

    @Override
    public String objTo(Long q1, int id, Long q2) {
        for (Map<String, Integer> map : SpringBootResource.getAqBagMapper().selectAq(q1)) {
            int oid = map.get("oid");
            if (oid == id) {
                SpringBootResource.getAqBagMapper().delete(map.get("id"));
                addToAqBgs(q2, id, map.get("num"));
                return "转让完成";
            }
        }
        return "您的背包里没有" + ID_2_NAME_MAPS.get(id);
    }
}
