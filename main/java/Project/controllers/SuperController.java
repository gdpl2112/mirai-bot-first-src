package Project.controllers;

import Project.aSpring.SpringBootResource;
import Project.commons.Father;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.UserScore;
import Project.controllers.auto.GameConfSource;
import Project.controllers.auto.TimerController;
import Project.dataBases.DataBase;
import Project.dataBases.GameDataBase;
import Project.dataBases.ShopDataBase;
import Project.dataBases.skill.SkillDataBase;
import Project.interfaces.Iservice.IGameService;
import Project.interfaces.Iservice.IManagerService;
import Project.listeners.SaveHandler;
import Project.services.impl.ZongMenServiceImpl;
import io.github.kloping.MySpringTool.StarterApplication;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.h1.impl.component.ActionManagerImpl;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.Main.iutils.MinecraftServerClient;
import io.github.kloping.mirai0.commons.*;
import Project.commons.broadcast.enums.ObjType;
import Project.commons.gameEntitys.ShopItem;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.object.ObjectUtils;
import io.github.kloping.serialize.HMLObject;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static Project.aSpring.SpringBootResource.getBagMapper;
import static Project.controllers.auto.GameConfSource.DELETE_MAX;
import static Project.dataBases.DataBase.HIST_U_SCORE;
import static Project.dataBases.DataBase.putInfo;
import static Project.dataBases.GameDataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.*;
import static io.github.kloping.mirai0.Main.iutils.MemberUtils.getUser;
import static Project.commons.resouce_and_tool.ResourceSet.FinalFormat.AT_FORMAT;
import static Project.commons.resouce_and_tool.ResourceSet.FinalString.*;

/**
 * @author github-kloping
 */
@Controller
public class SuperController {

    public static Map<String, Object> AUTO_CONF = new HashMap<>();
    public static String AUTO_CONF_PATH = "./conf/auto-conf.hml";

    static {
        try {
            AUTO_CONF = (Map<String, Object>) HMLObject.parseObject(FileUtils.getStringFromFile(AUTO_CONF_PATH)).toJavaObject();
            if (AUTO_CONF == null) AUTO_CONF = new HashMap<>();
            for (Field declaredField : GameConfSource.class.getDeclaredFields()) {
                String name = declaredField.getName();
                if (AUTO_CONF.containsKey(name)) {
                    Object v0 = AUTO_CONF.get(name);
                    if (v0 != null) {
                        declaredField.set(null, ObjectUtils.maybeType(v0.toString()));
                    }
                } else {
                    AUTO_CONF.put(name, declaredField.get(null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtils.putStringInFile(HMLObject.toHMLString(AUTO_CONF), new File(AUTO_CONF_PATH));
    }

    public long tempSuperL = -1;
    @AutoStand
    IGameService gameService;
    @AutoStand
    IManagerService managerService;
    @AutoStand
    private ZongMenServiceImpl zons;

    public SuperController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, SpGroup group, SpUser qq) throws NoRunException {
        if (tempSuperL != -1L) {
            if (qq.getId() == tempSuperL) {
                tempSuperL = -1L;
                return;
            }
        }
        if (!isSuperQ(qq.getId())) {
            throw new NoRunException("can`t do this");
        }
    }

    @Action("/get.+")
    public Object o0(@AllMess String str, SpGroup group) {
        long q0 = Project.utils.Utils.getAtFromString(str);
        if (q0 < 0) {
            return ERR_TIPS;
        }
        int n = Integer.parseInt(Tool.INSTANCE.findNumberFromString(str.replace(Long.toString(q0), "")));
        for (SaveHandler.AllMessage allMessage : SpringBootResource.getSaveMapper().selectMessage(group.getId(), q0, n)) {
            String s0 = allMessage.getContent();
            Message message;
            try {
                message = MessageChain.deserializeFromJsonString(s0);
            } catch (Exception e) {
                message = new PlainText(s0);
            }
            MessageUtils.INSTANCE.sendMessageInGroup(message, group.getId());
        }
        return "OK";
    }

    @Action("赋予一次超级权限.+")
    public String f0(@AllMess String s) {
        long q = Project.utils.Utils.getAtFromString(s);
        if (q == -1) {
            throw new NoRunException("");
        }
        tempSuperL = q;
        return "ok";
    }

    @Action(value = "addScore.{1,}", otherName = {"加积分.+"})
    public String addScore(@AllMess String messages, SpUser qq, SpGroup gr) throws NoRunException {
        long who = Project.utils.Utils.getAtFromString(messages);
        messages = messages.replace(Long.toString(who), "");
        if (who == -1) {
            return ERR_TIPS;
        }
        long num = Long.parseLong(Tool.INSTANCE.findNumberFromString(messages));
        DataBase.addScore(num, who);
        return new StringBuilder().append("给 =》 ").append(MemberUtils.getNameFromGroup(who, gr)).append("增加了\r\n=>").append(num + "").append("积分").toString();
    }

    @Action("全体加积分.{1,}")
    public String addAllScore(@AllMess String messages, SpUser qq) throws NoRunException {
        long num = Long.parseLong(Tool.INSTANCE.findNumberFromString(messages));
        HIST_U_SCORE.forEach((k, v) -> {
            v.addScore(num);
            putInfo(v);
        });
        return "完成!!";
    }

    @Action("全体加积分All.{1,}")
    public String addAllScore0(@AllMess String messages, SpUser qq) throws NoRunException {
        Long num = Long.parseLong(Tool.INSTANCE.findNumberFromString(messages));
        num = num == null ? 1 : num;
        for (UserScore score : SpringBootResource.getScoreMapper().selectAll()) {
            score.addScore(num);
            putInfo(score);
        }
        return "完成!!";
    }

    @Action("关机")
    public String k0() {
        Switch.AllK = false;
        return "已关机..";
    }

    @Action("开机")
    public String k1() {
        Switch.AllK = true;
        return "已开机..";
    }

    @Action("超级侦查<.+=>name>")
    public String select(SpUser qq, @AllMess String chain, SpGroup group) {
        long who = Project.utils.Utils.getAtFromString(chain);
        if (who == -1) return ("谁?");
        if (!GameDataBase.exist(who)) return (PLAYER_NOT_REGISTERED);
        PersonInfo I = GameDataBase.getInfo(qq.getId());
        PersonInfo Y = GameDataBase.getInfo(who);
        StringBuilder m1 = new StringBuilder();
        m1.append("ta的信息\n");
        String sss = gameService.info(who);
        m1.append(sss);
        return m1.toString();
    }

    @Action("更新宵禁<.+=>str>")
    public String a0(@Param("str") String str, SpGroup group) {
        return "ok";
    }

    @Action("添加管理.{1,}")
    public String addFather(@AllMess String message, SpUser qq, SpGroup group) throws NoRunException {
        if (!isSuperQ(qq.getId())) throw new NoRunException();
        long who = Project.utils.Utils.getAtFromString(message);
        if (who == -1) return "添加谁?";
        String perm = message.replace(String.format(AT_FORMAT, who), "");
        perm = perm.replace("添加管理", "");
        if (perm.equals(Father.ALL)) {
            return managerService.addFather(qq.getId(), who);
        } else {
            return managerService.addFather(qq.getId(), who, Long.toString(group.getId()));
        }
    }

    @Action("/c-ms")
    public String o2() {
        THREADS.submit(MinecraftServerClient.INSTANCE);
        return "trying";
    }

    @Action("/execute.+")
    public String o1(@AllMess String str, SpGroup group, BotEvent event) {
        long q = Project.utils.Utils.getAtFromString(str);
        if (q == -1) {
            throw new NoRunException("");
        }
        String qStr = q == BOT.getId() ? "me" : String.valueOf(q);
        str = str.replaceFirst("/execute\\[@" + qStr + "]", "");
        StarterApplication.executeMethod(q, str, q, getUser(q), SpGroup.get(group.getId()), 0, event);
        return null;
    }

    @Action("移除管理.{1,}")
    public String removeFather(@AllMess String message, SpUser qq) throws NoRunException {
        if (!isSuperQ(qq.getId())) throw new NoRunException();
        long who = Project.utils.Utils.getAtFromString(message);
        if (who == -1) return NOT_FOUND_AT;
        return managerService.removeFather(qq.getId(), who);
    }

    @Action("/即时公告<.+=>str>")
    public String announcement(@Param("str") String str) {
        for (net.mamoe.mirai.contact.Group group : BOT.getGroups()) {
            group.sendMessage(str);
        }
        return "ok";
    }

    @Action("/clearCache")
    public Object m4() {
        try {
            return HIST_U_SCORE.size() + " will clear\n" + HIST_INFOS.size() + " will clear";
        } finally {
            HIST_U_SCORE.clear();
            HIST_INFOS.clear();
            SkillDataBase.reMap();
            Tool.INSTANCE.deleteDir(new File("./temp"));
            MessageUtils.INSTANCE.HIST_IMAGES.clear();
        }
    }

    private static final int MAX_GET = 300;

    @Action("/添加物品<.+=>str>")
    public Object add0(@Param("str") String str) {
        Long who = Project.utils.Utils.getAtFromString(str);
        if (who == -1) {
            return NOT_FOUND_AT;
        }
        str = str.replace("[@" + who.toString() + "]", "");
        String what = str.trim().replaceAll(",", "").replaceAll("个", "");
        Integer num = null;
        try {
            num = Integer.valueOf(Tool.INSTANCE.findNumberFromString(what));
            what = what.replaceFirst(num + "", "");
        } catch (Exception e) {
            num = null;
        }
        num = num == null ? 1 : num > MAX_GET ? MAX_GET : num;
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(what);
        if (id == null) return ERR_TIPS;
        for (Integer integer = 0; integer < num; integer++) {
            getBagMapper().insertWithDesc(id, who.longValue(), System.currentTimeMillis(), "补偿添加");
        }
        return OK_TIPS;
    }

    @Action("/跳过闭关冷却.+")
    public String o1(@AllMess String l) {
        long who = Project.utils.Utils.getAtFromString(l);
        if (who == -1) return ERR_TIPS;
        getInfo(who).setBgk(0L).apply();
        return OK_TIPS;
    }

    @Action("/跳过进入冷却.+")
    public String oo1(@AllMess String mess) {
        try {
            String numStr = Tool.INSTANCE.findNumberFromString(mess);
            long qid = Long.parseLong(numStr);
            GameDataBase.getInfo(qid).setK2(-1L).apply();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    @Action("/更改武魂<.+=>mess>")
    public String modifyWuhun(@Param("mess") String mess) {
        long who = Project.utils.Utils.getAtFromString(mess);
        if (who == -1) return ERR_TIPS;
        mess = mess.replace("[@" + who + "]", "");
        Integer id = GameDataBase.NAME_2_ID_MAPS.get(mess);
        if (id == null) return ERR_TIPS;
        else if (id > 100) return ERR_TIPS;
        else {
            getInfo(who).setWh(id).apply();
            return OK_TIPS;
        }
    }

    @Action("/更改转生次数<.+=>mess>")
    public String modifyDeleteMax(@Param("mess") String mess) {
        Integer c = Tool.INSTANCE.getInteagerFromStr(mess);
        if (c == null) return ERR_TIPS;
        AUTO_CONF.put("DELETE_MAX", c);
        DELETE_MAX = c;
        FileUtils.putStringInFile(HMLObject.toHMLString(AUTO_CONF), new File(AUTO_CONF_PATH));
        return OK_TIPS;
    }

    @Action("/下架市场<.+=>str>")
    public String down(@Param("str") String str) {
        Integer id = Integer.parseInt(str);
        if (ShopDataBase.ITEM_MAP.containsKey(id)) {
            ShopItem item = ShopDataBase.ITEM_MAP.get(id);
            Long who = item.getWho().longValue();
            ShopDataBase.deleteItem(item.getId());
            addToBgs(who, item.getItemId(), item.getNum(), ObjType.un);
            return DOWN_SHOP_ITEM_OK;
        } else {
            return NOT_FOUND_SHOP_ITEM;
        }
    }

    @Action("/updateZero")
    public String uz() {
        TimerController.onZero();
        return "ok";
    }

    @Action("/list<.+=>str>")
    public Object l0(@Param("str") String str) {
        try {
            Map<String, Set<Method>> maps = new ConcurrentHashMap<>();
            Field field = ActionManagerImpl.class.getDeclaredField("maps");
            field.setAccessible(true);
            maps = (Map<String, Set<Method>>) field.get(StarterApplication.Setting.INSTANCE.getActionManager());
            StringBuilder sb = new StringBuilder();
            maps.forEach((k, v) -> {
                for (Method method : v) {
                    String dname = method.getDeclaringClass().getSimpleName();
                    if (dname.equalsIgnoreCase(str)) {
                        sb.append(k).append(NEWLINE);
                        return;
                    }
                }
            });
            return sb.toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return "not found";
    }

    @Action("/升级魂环<.+=>str>")
    public Object l1(@Param("str") String str) {
        Long q = Project.utils.Utils.getAtFromString(str);
        if (q == -1) {
            return NOT_FOUND_AT;
        }
        str = str.replace("[@" + q.toString() + "]", "");
        String what = str.trim().replaceAll("魂环", "").replaceAll("第", "");
        Integer st = Tool.INSTANCE.getInteagerFromStr(what);
        if (st == null) return ERR_TIPS;
        st--;
        Integer id = SpringBootResource.getHhpzMapper().select(q.longValue()).get(st);
        if (id == 207) return "TOP";
        else {
            GameDataBase.upHh(q, st, ++id);
            return "OK";
        }
    }

    @Action("/跳过冷却<.+=>str>")
    public Object l2(@Param("str") String str) {
        Long q = Project.utils.Utils.getAtFromString(str);
        if (q == -1) {
            return NOT_FOUND_AT;
        }
        getInfo(q).setK1(1L).setK2(1L).setGk1(1L).setCbk1(1L).setMk1(1L).setUk1(1L).setAk1(1L).setJak1(1L).apply();
        return OK_TIPS;
    }

    @Action("/addBuff<.+=>str>")
    public Object l3(@Param("str") String str) {
        Long q = Project.utils.Utils.getAtFromString(str);
        if (q == -1) {
            return NOT_FOUND_AT;
        }
        try {
            str = str.replaceAll("/addBuff", "");
            str = str.replace("[@" + q + "]", "");
            String[] sss = str.split("\\|");
            Integer v1 = Integer.valueOf(sss[1]);
            Integer v2 = Integer.valueOf(sss[2]);
            getInfo(q).addTag(sss[0], v1, v2).apply();
            return OK_TIPS;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
