package Project.controllers.normalController;

import Project.broadcast.PicBroadcast;
import Project.commons.GroupConf;
import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.UserScore;
import Project.commons.rt.ResourceSet;
import Project.dataBases.DataBase;
import Project.interfaces.Iservice.IOtherService;
import Project.interfaces.httpApi.Suning;
import Project.interfaces.httpApi.old.ApiIyk0;
import Project.services.detailServices.Idiom;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.Quiz;
import Project.utils.Tools.Tool;
import io.github.kloping.number.NumberUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.controllers.auto.ControllerTool.canGroup;
import static Project.controllers.normalController.CustomController.QLIST;
import static Project.controllers.normalController.CustomController.builderAndAdd;
import static Project.dataBases.DataBase.*;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;
import static io.github.kloping.mirai0.Main.BootstarpResource.Switch.AllK;
import static io.github.kloping.mirai0.Main.BootstarpResource.Switch.sendFlashToSuper;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class EntertainmentController {
    @AutoStand
    public static IOtherService otherService;
    public static int maxFail = 5;

    /**
     * 成语接龙消耗积分
     */
    private static int eveS1 = 2;
    public Map<Long, Idiom> longIdiomMap = new ConcurrentHashMap<>();
    @AutoStand
    Suning suning;
    @AutoStand
    private ApiIyk0 apiIyk0;

    public EntertainmentController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group, @AllMess String mess) throws NoRunException {
        if (!AllK) {
            throw new NoRunException();
        }
        if (mess.startsWith("[闪照")) {

        } else {
            if (!canGroup(group.getId())) {
                throw new NoRunException();
            }
        }
    }

    @Action("\\[Pic:.+")
    public String onPic(@AllMess String mess, SpGroup group, Object[] objects, long qq) {
        PicBroadcast.INSTANCE.broadcast(qq, group.getId(), mess, objects);
        if (QLIST.containsKey(qq)) {
            String str = QLIST.get(qq);
            str = str.replaceFirst("\\*", mess);
            if (str.contains("*")) {
                QLIST.remove(qq);
                QLIST.put(qq, str);
                return "已填充1个";
            } else {
                QLIST.remove(qq);
                if (builderAndAdd(str, qq, group.getId())) {
                    return "填充完成\r\n添加完成";
                } else {
                    return ResourceSet.FinalString.ADD_TO_AUTO_REPLY_ERROR;
                }
            }
        } else {
            throw new NoRunException("没有在添加");
        }
    }

    @Action("时间")
    public Object nowTime() {
        return Tool.INSTANCE.getTimeYMdhms(
                suning.ct().getLong("currentTime"));
    }

    @Action("传话<.+=>str>")
    public Object transTo(@Param("str") String str, SpGroup group, Long qq) {
        return otherService.trans(str, group, qq);
    }

    @Action("语音")
    public String a1(SpGroup group) {
        GroupConf conf = getConf(group.getId());
        conf.setVoiceK(conf.getVoiceK() ? false : true);
        setConf(conf);
        return conf.getVoiceK() ? "当前开启" : "当前关闭";
    }

    @Action(value = "掷骰子", otherName = "摇骰子")
    public String rand(SpGroup group) {
        int r = Tool.INSTANCE.RANDOM.nextInt(6) + 1;
        return Tool.INSTANCE.pathToImg(String.format("http://kloping.top/R" + r + ".jpg"));
    }

    @Action("开始成语接龙")
    public String s1(SpGroup group) {
        if (longIdiomMap.containsKey(group.getId())) {
            return "游戏已经开始了哦~";
        }
        Idiom idiom = new Idiom() {
            @Override
            public void fail(String s) {
                longIdiomMap.remove(group.getId());
            }
        };
        longIdiomMap.put(group.getId(), idiom);
        return "游戏开始:\n当前成语: " + idiom.getRandom() + "\n命令:我接xxxx";
    }

    @Action("我接<.+=>str>")
    public String s2(@Param("str") String str, SpGroup group, SpUser user) {
        Idiom idiom = longIdiomMap.get(group.getId());
        if (idiom == null) {
            return "游戏未开始:请说 开始成语接龙";
        }
        UserScore score = DataBase.getUserInfo(user.getId());
        if (score.getScore() < eveS1) {
            return "您的积分不足...";
        }
        String s = idiom.meet(str);
        StringBuilder sb = new StringBuilder();
        switch (s) {
            case "-1":
                sb.append("必须是4个字的成语哦\n");
                sb.append("扣除1积分");
                score.addScore(-1);
                DataBase.putInfo(score);
                break;
            case "-2":
                sb.append("\"").append(str);
                sb.append("\"好像不是一个成语呢\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.addScore(-eveS1);
                DataBase.putInfo(score);
                break;
            case "-3":
                sb.append("音节好像不对哦\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.addScore(-eveS1);
                DataBase.putInfo(score);
                break;
            case "-4":
                sb.append("这个词已经用过了哦\n");
                sb.append("扣除").append(eveS1).append("积分");
                score.addScore(-eveS1);
                DataBase.putInfo(score);
                break;
            default:
                sb.append(s);
                sb.append("接上了\n");
                sb.append("获得").append(eveS1 * 2).append("积分");
                score.addScore(eveS1 * 2);
                DataBase.putInfo(score);
                break;
        }
        sb.append("\n第").append(idiom.getHist().size()).append("次的接龙");
        sb.append("\n当前成语: ").append(idiom.getUpWord()).append("\n")
                .append("末尾音节: ").append(idiom.getUpPinYin());
        if (!longIdiomMap.containsKey(group.getId())) {
            sb.append("\n游戏结束!!");
            sb.append("\n=========\n");
            for (String s1 : idiom.getHist()) {
                sb.append(s1).append("=>");
            }
            if (sb.toString().endsWith("=>")) {
                sb = sb.replace(sb.length() - 2, sb.length(), "");
            }
        }
        return sb.toString();
    }

    @Action("竞猜.+")
    public String s3(long qid, @AllMess String mess) {
        if (!Quiz.quiz.isProcessing())
            return "竞猜已停止";
        StringBuilder sb = new StringBuilder();
        String[] sss = mess.substring(2).split(",|，");
        Integer index = Tool.INSTANCE.getInteagerFromStr(sss[0]);
        Integer sc = Tool.INSTANCE.getInteagerFromStr(sss[1]);
        sc = sc == null ? 0 : sc;
        if (sc > 10000) {
            sc = 10000;
            sb.append("最大竞猜1000自动转换\n");
        } else if (sc < 10) {
            sc = 10;
            sb.append("最小竞猜10自动转换\n");
        }
        if (Quiz.quiz.append(qid, index, sc)) {
            UserScore userScore = DataBase.getUserInfo(qid);
            if (userScore.getScore() < sc) return "积分不足";
            userScore.addScore(-sc);
            int all = Quiz.quiz.getAll();
            sb.append(Quiz.quiz.getTitle()).append(NEWLINE);
            Quiz.quiz.getQuizData().forEach((k, v) -> {
                int a0 = Quiz.quiz.getAll(k);
                int b = NumberUtils.toPercent(a0, all);
                sb.append(k).append(v).append("==>>").append(b).append("%").append("(").append(a0).append(")\n");
            });
            return sb.toString();
        } else {
            return "竞猜失败,最大三次竞猜";
        }
    }
}
