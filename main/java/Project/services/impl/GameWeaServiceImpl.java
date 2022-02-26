package Project.services.impl;

import Project.services.detailServices.GameWeaDetailService;
import Project.interfaces.Iservice.IGameWeaService;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.dataBases.GameDataBase.*;
import static Project.dataBases.SourceDataBase.getImgById;
import static io.github.kloping.mirai0.unitls.drawers.Drawer.getImageFromStrings;

/**
 * @author github-kloping
 */
@Entity
public class GameWeaServiceImpl implements IGameWeaService {

    @AutoStand
    public static GameWeaDetailService gameWeaDetailService;

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

    private static final Pattern PATTERN = Pattern.compile("(\\[\\@.+\\]|#)");

    public static List<String> getLps(String ss) {
        List<String> list = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(ss);
        while (matcher.find()) {
            list.add(matcher.group().trim());
        }
        return list;
    }

    @Override
    public String aqBgs(Long who) {
        List<String> list = new ArrayList<>();
        Map<Integer, Map.Entry<Integer, Integer>> maps = getBgsw(who);
        if (maps.isEmpty()) {
            return "你没有暗器!";
        }
        for (Integer i : maps.keySet()) {
            list.add(i + "=>" + getNameById(maps.get(i).getKey()) + ":剩余" + maps.get(i).getValue() + "次");
        }
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
            return "制作成功" + getImgById(id) + "\r\n" + ID_2_INTRO_MAPS.get(id);
        } else {
            return "\"" + getNameById(id) + "\"制作的暗器零件不足,需要" + ns + "个";
        }
    }

    public static String MENU = "";

    static {
        MENU += "\n#选择器(#为当前魂兽))";
        MENU += "\n使用暗器<暗器名><选择器>";
        MENU += "\n暗器背包";
        MENU += "\n制作暗器<暗器名>";
        MENU += "\n暗器制作表";
    }

    @Override
    public String aqMeun() {
        return MENU.trim();
    }

    @Override
    public String aqList() {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Integer i : ID_2_WEA_MAPS.keySet()) {
            list.add("\"" + getNameById(i) + "\"需要" + ID_2_WEA_MAPS.get(i) + "个零件");
            sb.append("\"" + getNameById(i) + "\"需要" + ID_2_WEA_MAPS.get(i) + "个零件").append("\r\n");
        }
        return getImageFromStrings(list.toArray(new String[0])) + "\r\n" + sb;
    }
}
