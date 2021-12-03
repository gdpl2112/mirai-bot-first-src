package Project.Services.impl;

import Project.Services.DetailServices.GameWeaDetailService;
import Project.Services.Iservice.IGameWeaService;
import Project.broadcast.enums.ObjType;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.DataBases.GameDataBase.*;
import static Project.Tools.Drawer.getImageFromStrings;

@Entity
public class GameWeaServiceImpl implements IGameWeaService {


    @AutoStand
    static GameWeaDetailService gameWeaDetailService;

    @Override
    public String UseAq(String str, Long who) {
        List<String> list = getLps(str);
        for (int i = list.size() - 1; i > -1; i--) {
            String s1 = list.get(i);
            str = str.replace(s1, "");
            list.remove(s1);
            list.add(s1.replace("[@", "").replace("]", ""));
        }
        return gameWeaDetailService.UseAq(list, who, str.trim());
    }

    private static final Pattern pattern = Pattern.compile("(\\[\\@.+\\]|#)");

    public static List<String> getLps(String ss) {
        List<String> list = new ArrayList<>();
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            list.add(matcher.group().trim());
        }
        return list;
    }

    @Override
    public String AqBgs(Long who) {
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
        int ns = id2WeaMaps.get(id);
        if (num >= ns) {
            addToAqBgs(who, id + ":" + (id2WeaONumMaps.get(id)));
            for (int i = 0; i < ns; i++) {
                removeFromBgs(who, 1000, ObjType.use);
            }
            return "制作成功" + getImgById(id) + "\r\n" + id2IntroMaps.get(id);
        } else {
            return "\"" + getNameById(id) + "\"制作的暗器零件不足,需要" + ns + "个";
        }
    }

    @Override
    public String AqMeun() {
        List<String> list = new ArrayList<>();
        list.add("(选择器(#为当前魂兽))");
        list.add("使用暗器(暗器名)(选择器)");
        list.add("暗器背包");
        list.add("制作暗器 (暗器名)");
        list.add("暗器制作表");
        return getImageFromStrings(list.toArray(new String[0]));
    }

    @Override
    public String AqList() {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Integer i : id2WeaMaps.keySet()) {
            list.add("\"" + getNameById(i) + "\"需要" + id2WeaMaps.get(i) + "个零件");
            sb.append("\"" + getNameById(i) + "\"需要" + id2WeaMaps.get(i) + "个零件").append("\r\n");
        }
        return getImageFromStrings(list.toArray(new String[0])) + "\r\n" + sb;
    }
}
