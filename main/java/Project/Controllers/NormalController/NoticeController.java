package Project.Controllers.NormalController;

import Entitys.Group;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.opened;
import static Project.ResourceSet.FinalValue.*;
import static io.github.kloping.Mirai.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class NoticeController {
    public NoticeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    private static final String HJ_INTRO = "精神力有什么用:\r\n\t" +
            "   1.精神力高于80%时格挡10%的伤害,同时消耗精神力\r\n\t" +
            "   2.精神力低于45%时额外受到10%的伤害\r\n\t" +
            "   3.魂兽也存在精神力,若魂兽的精神力与你精神力差不多,魂兽可凭借精神力隐藏自己的实力,可使用(探查)消耗精神力探查其状态" +
            "   \n==========\n" +
            "   4.可使用 精神攻击@某 随机发射 12~20%(可指定) 的最大精神力的值 对目标造成同等值的精神损失 最大造成 " + MAX_SA_LOSE_HJ_B + "%的精神力" +
            "若目标不足及承受发射的精神力 则将额外造成 剩余可作用的精神力的值的" + HJ_LOSE_1_X +
            "倍的伤害最大造成目标" + MAX_SA_LOSE_HP_B + "%的最大生命值";

    @Action("精神力作用")
    public Object more1() {
        return HJ_INTRO;
    }

    private static final StringBuilder UPDATE_LOG = new StringBuilder();
    private static String[] UPDATE_LOGS;

    static {
        UPDATE_LOG.append("==========").append("\n");
        UPDATE_LOG.append("1.26:修复已知bug,新的周任务,精神力伤害的调整,魂技效果加成的调整").append("\n");
        UPDATE_LOG.append("1.25:修复已知bug,新年样式#狗头保命").append("\n");
        UPDATE_LOG.append("1.23: 修复永久加攻击的bug").append("\n");
        UPDATE_LOG.append("1.23:批量抢劫最大12").append("\n");
        UPDATE_LOG.append("1.23:介于 精神攻击的 刮痧,对其进行了调整,详情见\"精神力作用\"").append("\n");
        UPDATE_LOG.append("1.22:新娱乐功能,/滚草<At/图片>").append("\n");
        UPDATE_LOG.append("1.21:修复已知bug").append("\n");
        UPDATE_LOG.append("1.20:修复已知bug").append("\n");
        UPDATE_LOG.append("1.18:优化显示,修复已知bug").append("\n");
        UPDATE_LOG.append("1.18:魂兽蓄力 时 攻击减少 12%").append("\n");
        UPDATE_LOG.append("1.17:优化代码 修复 已知bug 一些事件的 调整").append("\n");
        UPDATE_LOG.append("1.16:调整魂兽生成(等级)").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("1.15:修复已知bug 削弱魂兽 蓄力伤害 削弱生成魂兽").append("\n");
        UPDATE_LOG.append("1.14:修复已知bug,新娱乐功能: 网易云热评").append("\n");
        UPDATE_LOG.append("1.12:修复已知bug").append("\n");
        UPDATE_LOG.append("1.11-v2:修复已知bug,技能效果可叠加,魂兽的削弱,魂技的更新").append("\n");
        UPDATE_LOG.append("1.11: 精神攻击<At/#><V> 或 精神攻击<At/#><V> 详情见\"精神力作用\"").append("\n");
        UPDATE_LOG.append("1.6 : 娱乐功能 变大<-size=value>").append("\n");
        UPDATE_LOG.append("1.5 : 更新了 更新日志").append("\n");
        UPDATE_LOG.append("1.3 : 修复已知bug").append("\n");
        UPDATE_LOG.append("1.2 : 每天0点更新排行,(提升性能").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("12.31: 今年最后一次更新->修复已知bug").append("\n");
        UPDATE_LOG.append("12.30: 王者语音<名字>(n) 王者图片<名字>").append("\n");
        UPDATE_LOG.append("12.26: 金魂币消费记录").append("\n");
        UPDATE_LOG.append("12.26: 修复已知bug").append("\n");
        UPDATE_LOG.append("12.24: 原神公告<n> 王者公告<n>").append("\n");
        UPDATE_LOG.append("12.23: 批量抢劫").append("\n");
        UPDATE_LOG.append("12.22: 催更,开发计划,王者荣耀最新公告/王者公告 ").append("\n");
        UPDATE_LOG.append("12.21: 修复已知bug 微调 魂兽概率").append("\n");
        UPDATE_LOG.append("12.20: 修复同时完成任务的bug,新的每周任务").append("\n");
        UPDATE_LOG.append("12.18: 修复已知bug,百科<名字>").append("\n");
        UPDATE_LOG.append("12.18: 新增娱乐功能 捡瓶子/捡漂流瓶  扔瓶子/仍漂流瓶<内容>").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("12.16: 修复已知bug, 模糊进入\n\t#进入星斗大森林,进入星斗,进入极北\n- 接每周任务\r\n\t #每周任务在每周早刷新,现只有一个任务,之后会继续更新任务").append("\n");
        UPDATE_LOG.append("12.14: 修复已知bug,升级第<几>魂环,合成<物品名>\n\t接徒弟任务,当前任务 #测试阶段  ").append("\n");
        UPDATE_LOG.append("12.13: 修复已知bug,别戳我 ").append("\n");
        UPDATE_LOG.append("12.11: /丢@xx ").append("\n");
        UPDATE_LOG.append("12.11: 新物品,(白/黄/紫/黑/红)升级券 获取方式后续更新,降低名师点要求 ").append("\n");
        UPDATE_LOG.append("12.9 : 中级魂骨可出售,降低名师点要求 ").append("\n");
        UPDATE_LOG.append("12.6 : 魂技使用次数,降低名师点要求").append("\n");
        UPDATE_LOG.append("12.5 : 魂技加血/魂力 技能 以自身值为基础 且 不会为 被增益者 回复超过最大值一半的量").append("\n");
        UPDATE_LOG.append("12.4 : 验证码忽略大小写/可 看不清 ").append("\n");
        UPDATE_LOG.append("12.3 : 进群验证(开关 (开启/关闭)验证 ").append("\n");
        UPDATE_LOG.append("12.2 : 详细信息 收徒@xx 出师").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("11.29: /推@ ").append("\n");
        UPDATE_LOG.append("11.27: 开始成语接龙 (完善 ").append("\n");
        UPDATE_LOG.append("11.26: 一些 欢迎啥的 ").append("\n");
        UPDATE_LOG.append("11.25: 看似更新了 其实没更新 ").append("\n");
        UPDATE_LOG.append("11.23: 修复已知 Bug , 融合关系 转移至 \"关系列表\" ").append("\n");
        UPDATE_LOG.append("11.22: 测试性 功能 创建分身 ").append("\n");
        UPDATE_LOG.append("11.20:修复点歌, \"排行\" 改为 \"等级排行\" ").append("\n");
        UPDATE_LOG.append("11.19: 大无语事件的发生 与 解决 ").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("11.13.19: 修复反甲不能打魂兽的Bug ").append("\n");
        UPDATE_LOG.append("11.13: 修复已知Bug, 菜单变动 魂兽生成削弱 魂兽锁定条件削弱").append("\n");
        UPDATE_LOG.append("11.5: 尝试修复魂兽击杀排行的 Bug").append("\n");
        UPDATE_LOG.append("11.4: 修复注册失败的 Bug").append("\n");
        UPDATE_LOG.append("11.3: 我的魂技 修复已知Bug").append("\n");
        UPDATE_LOG.append("11.1: 修复-已知Bug.V2 添加血量为空降级的冷却\n\t降低魂兽匹配实力\n\t选择攻击伤害0.33~0.44提升至0.35~0.48").append("\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("10.31: 修复 闪照破解\n\t 优化性能 且测试").append("\n");
        UPDATE_LOG.append("10.27: 修复魂技 异常 和 语音 不全的Bug").append("\n");
        UPDATE_LOG.append("10.26:开始会话 结束会话").append("\n");
        UPDATE_LOG.append("10.23: 尝试修复 选择攻击发不出的情况").append("\n");
        UPDATE_LOG.append("10.21: 解析抖音图片 地址\n\t撤回功能实现\n");
        UPDATE_LOG.append("10.17: 解析快手图片 地址\n");
        UPDATE_LOG.append("[nextPage]").append("\n");
        UPDATE_LOG.append("10.13: 魂兽击杀排行 修复 显示魂力消耗问题\n");
        UPDATE_LOG.append("10.10:魂力消耗调整,暗器增加伤害上限,暗器制作成本调整,魂技增幅属性调整\n见暗器制作表\n魂技增益性不会超过本身最大值").append("\r\n");
        UPDATE_LOGS = UPDATE_LOG.toString().split("\\[nextPage]\\n");
    }

    private static final String MORA_STR_2 = "特别的,魂环吸收最低等级限制:\r\n\t" +
            "   9级不能吸收万年及以上\n\t" +
            "   49级不能吸收十万年及以上\n\t" +
            "   69级不能吸收百万年及以上\n\t";

    @Action("更新日志.*?")
    public String updateLog(@AllMess String m) {
        Integer i = 0;
        i = Tool.getInteagerFromStr(m);
        i = i == null || i >= UPDATE_LOGS.length ? 0 : i;
        return UPDATE_LOGS[i];
    }

    @Action("魂环吸收限制")
    public Object more2() {
        return MORA_STR_2;
    }

    private static final String STRING = "每个人 今日首次 无状态时(血量为0)>清空经验,\n" +
            "再次 无状态时 下降一级,\n" +
            "之后都 清空经验\n" +
            "===============\n" +
            "每日最多下降一级\n" +
            "整10级(10,20...)时不会下降等级\n" +
            "请保证自己的血量健康";

    @Action("新机制")
    public String m1() {
        return STRING;
    }

    public static int lowst = 5;

    @Action(value = "怎么获得名师点", otherName = {"名师点.+"})
    public String m2() {
        return "每单独击杀 一只" + lowst + "w 或 以上级别的魂兽 增加一点名师点";
    }
}
