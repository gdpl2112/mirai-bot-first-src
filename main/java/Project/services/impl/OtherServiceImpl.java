package Project.services.impl;


import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IOtherService;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.Group;
import io.github.kloping.mirai0.commons.Mora;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import static Project.controllers.auto.ControllerTool.canGroup;
import static Project.dataBases.DataBase.isFather;
import static io.github.kloping.mirai0.Main.ITools.MessageTools.*;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.ERR_TIPS;

/**
 * @author github-kloping
 */
@Entity
public class OtherServiceImpl implements IOtherService {

    private static String BasicUrl = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    private static String filter(String result) {
        int start = result.indexOf(":\"");
        result = result.replaceAll("\\{", "<");
        result = result.replaceAll("\\}", ">");
        result = result.replaceAll("f", "F");
        result = result.substring(start + 2, result.lastIndexOf("\""));
        result = result.replaceAll("菲菲", Resource.MY_MAME);
        result = result.replaceAll("<br>", "\r\n");
        return result.trim();
    }

    @Override
    public String mora(Long who, String what) {
        long li = DataBase.getAllInfo(who).getScore();
        String num = Tool.findNumberFromString(what);
        long l1 = num.isEmpty() ? 0 : Long.parseLong(num);
        Mora mora1 = Mora.findMora(what, 0);
        if (mora1 == null || mora1.getValue().isEmpty())
            return "猜拳格式错误=> 猜拳 (石头/剪刀/布) (积分)";
        if (li < l1)
            return "积分不足!";
        if (l1 < 5)
            return "积分最小值:5";
        if (l1 > 1500)
            return "积分最大值:1500";
        Mora i = Mora.getRc(48, 10, mora1);
        int p = mora1.Reff(i);
        if (p == 0) {
            return "平局 我出的是" + i.getValue();
        } else if (p == -1) {
            DataBase.addScore(-l1, Long.valueOf(who));
            return "你输了 我出的是" + i.getValue() + "\n你输掉了:" + l1 + "积分";
        } else if (p == 1) {
            DataBase.addScore(l1, Long.valueOf(who));
            return "你赢了 我出的是" + i.getValue() + "\n你获得了:" + l1 + "积分";
        }
        return "猜拳异常";
    }

    @Override
    public String talk(String str) {
        try {
            URL url = new URL(BasicUrl + java.net.URLEncoder.encode(str, "utf-8"));
            InputStream is = url.openStream();
            BufferedInputStream br = new BufferedInputStream(is);
            byte[] bytes1 = new byte[1024 * 4];
            br.read(bytes1);
            String result = new String(bytes1, "utf-8").trim();
            result = filter(result);
            if (Tool.isIllegSend(result)) {
                return ERR_TIPS;
            }
            return result.trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String trans2(String str, Group group, Long qq) {
        if (!isFather(qq)) {
            throw new NoRunException();
        }
        try {
            str = str.replace("[", "").replace("@", "").replace("]", "");
            if (Tool.isIlleg(str))
                return "存在敏感字符";
            if (str.contains("到")) {
                String[] ss = str.split("到");
                Long gr = Long.valueOf(ss[1]);
                if (isJoinGroup(gr)) {
                    StringBuilder builder = new StringBuilder();
                    sendMessageInGroup(ss[0], gr);
                    return "传话成功!";
                } else {
                    return "抱歉了,我没有加入该群!";
                }
            } else if (str.contains("给")) {
                String[] ss = str.split("给");
                Long gq = Long.valueOf(ss[1]);
                if (containsOneInGroup(qq, group.getId())) {
                    sendMessageInOneFromGroup(ss[0], gq, group.getId());
                    return "传话成功!";
                } else {
                    return "未查找到此人";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "示例:\r\n" +
                "传话到群: 传话你好啊到278681553\r\n" +
                "传话到人: 传话你好啊给(at) 或\r\n\t 传话你好啊给(QQ号)";
    }


    @Override
    public String trans(String str, Group group, Long qq) {
        try {
            str = str.replace("[", "").replace("@", "").replace("]", "");
            if (Tool.isIlleg(str))
                return "存在敏感字符";
            if (str.contains("到")) {
                String[] ss = str.split("到");
                Long gr = Long.valueOf(ss[1]);
                if (!canGroup(qq)) {
                    return "那个群-把我关闭了 呜呜~";
                }
                if (isJoinGroup(gr)) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("来自群:").append(group.getId() + "").append("\r\n的").append(qq + "").append("传话:\r\n===================\r\n");
                    builder.append(ss[0]);
                    sendMessageInGroup(builder.toString(), gr);
                    return "传话成功!";
                } else {
                    return "抱歉了,我没有加入该群!";
                }
            } else if (str.contains("给")) {
                String[] ss = str.split("给");
                Long gq = Long.valueOf(ss[1]);
                if (containsOneInGroup(gq, group.getId())) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("来自群:").append(group.getId() + "").append("\r\n的").append(qq + "").append("传话:\r\n===================\n");
                    builder.append(ss[0]);
                    sendMessageInOneFromGroup(builder.toString(), gq, group.getId());
                    return "传话成功!";
                } else {
                    return "未查找到此人";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "示例:\r\n" +
                "传话到群: 传话你好啊到278681553\r\n" +
                "传话到人: 传话你好啊给(at) 或\r\n\t 传话你好啊给(QQ号)";
    }
}
