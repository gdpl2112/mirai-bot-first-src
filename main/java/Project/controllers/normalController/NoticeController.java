package Project.controllers.normalController;

import Project.commons.SpGroup;
import Project.services.player.UseRestrictions;
import Project.utils.VelocityUtils;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import Project.utils.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalString.NEWLINE;
import static Project.commons.rt.ResourceSet.FinalValue.*;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class NoticeController {
    public static final int LOWST = 5;
    private static final StringBuilder UPDATE_LOG = new StringBuilder();
    private static String[] UPDATE_LOGS;

    static {
        UPDATE_LOG.append("==========").append(NEWLINE);
        UPDATE_LOG.append(" 8.24:派遣奖励增加;派遣[1天/2天][大瓶经验/时光胶囊/金魂币]").append(NEWLINE);
        UPDATE_LOG.append(" 8.23:派遣功能;派遣[1天/2天][大瓶经验/时光胶囊/金魂币]").append(NEWLINE);
        UPDATE_LOG.append(" 8.12:修复部分bug").append(NEWLINE);
        UPDATE_LOG.append(" 6.25:尝试排序交易市场").append(NEWLINE);
        UPDATE_LOG.append(" 6.25:活动结束通告").append(NEWLINE);
        UPDATE_LOG.append(" 6.21:'兑换奖券'改为'购买奖券'").append(NEWLINE);
        UPDATE_LOG.append(" 6.21:修复活动未开启问题").append(NEWLINE);
        UPDATE_LOG.append(" 6.20:端午活动;命令'划龙舟';'活动列表'更名'副本列表';'活动列表'").append(NEWLINE);
        UPDATE_LOG.append(" 6.18:命令修复").append(NEWLINE);
        UPDATE_LOG.append(" 6.11:修复宗门奖励异常的问题,部分合成分解可批量").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append(" 5.31:挂机奖励上限最多14天").append(NEWLINE);
        UPDATE_LOG.append(" 5.23:兑换奖券上限为7;修复部分bug;宗门活跃不足50不予奖励").append(NEWLINE);
        UPDATE_LOG.append(" 5.16:荣耀查询功能;邀请奖励").append(NEWLINE);
        UPDATE_LOG.append(" 5.05:修复已知bug,兑换奖券价格下调至2000").append(NEWLINE);
        UPDATE_LOG.append(" 5.03:活动结束,更新每周可`兑换奖券` 10000金魂币兑换一张 上限五张 每周刷新").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append(" 4.29:第二武魂开放`激活第二武魂`以激活;抽奖机制升级,详情见奖池;5.1活动,每击杀一只十万年或以上的魂兽奖励1~2张奖券活动至5.3").append(NEWLINE);
        UPDATE_LOG.append(" 4.24:切换武魂,激活第二武魂,抽奖更新,将[奖池,抽奖,抽奖十连],第二武魂测试/").append(NEWLINE);
        UPDATE_LOG.append(" 4.17:成就更新,当选择攻击魂兽时魂兽不随着玩家的选择攻击而攻击玩家,转为魂兽自主的向玩家攻击").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
//        UPDATE_LOG.append(" 3.24:修改魂环吸收概率,每次吸收失败增加10%").append(NEWLINE);
        UPDATE_LOG.append(" 3.15:开放'创建试炼挑战','创建平衡挑战'").append(NEWLINE);
        UPDATE_LOG.append(" 3.12:放弃任务功能").append(NEWLINE);
        UPDATE_LOG.append(" 2.18:代码重构").append(NEWLINE);
        UPDATE_LOG.append(" 2. 8:修复部分部分魂技介绍异常的的问题;每日打卡自动[签到|魂师签到]").append(NEWLINE);
        UPDATE_LOG.append(" 2. 6:异步转让与异步出售#不在有出售/转让数量限制").append(NEWLINE);
        UPDATE_LOG.append(" 2. 2:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append(" 1.29:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append(" 1.28:修复部分bug;魂技更新;<宗门转让>功能").append(NEWLINE);
        UPDATE_LOG.append(" 1.27:宗门更新;见<宗门系统>").append(NEWLINE);
        UPDATE_LOG.append(" 1.27:修复部分已知bug,见<魂技更新列表>").append(NEWLINE);
        UPDATE_LOG.append(" 1.19:落日魂兽新增 泰坦巨猿 天青牛蟒 及专属魂骨,魂技技能").append(NEWLINE);
        UPDATE_LOG.append(" 1.10:每周魂师签到连续奖励").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("12.31:红包退还功能;魂师签到").append(NEWLINE);
        UPDATE_LOG.append("12.24:抢红包功能更新,圣诞活动(12.25),活动当天6-22时,每小时随机发放随机红包,当前若存在红包将覆盖").append(NEWLINE);
        UPDATE_LOG.append("12.17:王者语音更新").append(NEWLINE);
        UPDATE_LOG.append("12.16:闭关更新").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("11.26:问题修复").append(NEWLINE);
        UPDATE_LOG.append("11.20:成就更新,转生上限修改").append(NEWLINE);
        UPDATE_LOG.append("11.19:修复部分魂兽丢失bug").append(NEWLINE);
        UPDATE_LOG.append("11.07:解析视频音频 解析图集音频 支持抖音快手").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("10.13:尝试修复已知bug,优化魂兽信息显示").append(NEWLINE);
        UPDATE_LOG.append("10.5 :<抽奖十连>").append(NEWLINE);
        UPDATE_LOG.append("10.1 :活动,魂兽几率掉落抢券,可用来<抽奖>限时活动,<奖池>").append(NEWLINE);
        UPDATE_LOG.append("9.30 :星斗魂兽种类等级分类见'魂兽列表',新物品<魂兽挑战券>").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("9.23:等级排行显示优化").append(NEWLINE);
        UPDATE_LOG.append("9.18:调整修复部分功能").append(NEWLINE);
        UPDATE_LOG.append("9.12:中秋活动结束,<信息预览><扔/捡瓶子> 功能恢复 ").append(NEWLINE);
        UPDATE_LOG.append("9.9 :中秋活动,<修炼><双修><进入><击败魂兽>都有几率获得月饼,月饼可用于限时活动的<兑换>,详情见<兑换列表>").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("8.30:探查时可见魂兽buff").append(NEWLINE);
        UPDATE_LOG.append("8.21:降低部分魂技伤害加成,修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("8.19:极北之地 魂骨掉率提升,但极北魂兽新增极北特定魂技").append(NEWLINE);
        UPDATE_LOG.append("8.13:徒弟可收多个120级2个,150级3个,出徒<N> 出从上到下第N个徒弟").append(NEWLINE);
        UPDATE_LOG.append("8.10:'捐款'").append(NEWLINE);
        UPDATE_LOG.append("8.8 :商城上架净化药水,调整部分功能").append(NEWLINE);
        UPDATE_LOG.append("8.4 :成就系统/其命令[成就列表<Page>,领取成就<Id>],详细信息更多信息").append(NEWLINE);
        UPDATE_LOG.append("8.3 :移除部分无用功能").append(NEWLINE);
        UPDATE_LOG.append("8.2 :部分(玩家,魂兽)魂技调整,遇魂兽使用上限改为7次,'暗器背包'更名为'武器背包',武器背包中物品可被转让,,新物品[变异魂环,普通魂导材料,中级魂导材料,高级魂导材料,魂导护盾,高级魂导护盾,魂导炮,高级魂导炮]详细作用请见说明").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("7.27:背包0 查看全部物品,尝试修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("7.25:尝试修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("7.18:背包分页(背包1,背包2)").append(NEWLINE);
        UPDATE_LOG.append("7.16:尝试修复已知问题,新物品净化药水").append(NEWLINE);
        UPDATE_LOG.append("7.13:尝试修复已知Bug,积分排行").append(NEWLINE);
        UPDATE_LOG.append("7.11:尝试修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("7.10:尝试修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("7.9: 修复已知问题,修复攻击魂兽触发2次的bug,魂兽新增魂技").append(NEWLINE);
        UPDATE_LOG.append("7.8: 修复已知问题,魂兽新·魂技").append(NEWLINE);
        UPDATE_LOG.append("7.2: 到达神王之后经验不会掉").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("6.29:新增魂力节省比:随等级成长最大50%,重写buff算法,修复问题").append(NEWLINE);
        UPDATE_LOG.append("6.27:修复部分魂技问题").append(NEWLINE);
        UPDATE_LOG.append("6.24:移除玩家攻击玩家掉级的设定").append(NEWLINE);
        UPDATE_LOG.append("6.23:修复部分已知问题,排行同等级按照经验排序").append(NEWLINE);
        UPDATE_LOG.append("6.19:修复部分已知问题").append(NEWLINE);
        UPDATE_LOG.append("6.16:修复已知问题,见'挑战说明'").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("6.15:取消'攻击',新增试炼挑战,见'挑战说明',存在bug请及时反馈,修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("6.11:尝试修复了跨宗门可设置长老的问题").append(NEWLINE);
        UPDATE_LOG.append("6.10:尝试修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("6.9 :暗器使用增加冷却,冷却为1/3的后摇;见魂技更新列表").append(NEWLINE);
        UPDATE_LOG.append("6.6 :尝试修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("6.4 :尝试修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("6.3 :尝试修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("6.2 :端午活动,每日可\'领取粽子\',击败魂兽几率掉落粽子6.2-6.5").append(NEWLINE);
        UPDATE_LOG.append("6.1 :取消魂技类型的描述").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("5.31:尝试解决同一魂技攻击无限叠加的问题").append(NEWLINE);
        UPDATE_LOG.append("5.29:修复部分问题").append(NEWLINE);
        UPDATE_LOG.append("5.28:修复已知问题 ").append(NEWLINE);
        UPDATE_LOG.append("5.27:我的buff,我的效果,第八魂技开始测试,有些武魂有2种第八魂技,但大部分只有1种,后期可能会增加,若您有好的建议,可以给开发者提供,第八魂技可能存在bug,发现bug请及时反馈").append(NEWLINE);
        UPDATE_LOG.append("5.25:修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("5.24:修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("5.21:修复已知问题").append(NEWLINE);
        UPDATE_LOG.append("5.19:修复了转生之后第一次就遇十万的问题").append(NEWLINE);
        UPDATE_LOG.append("5.18:每日可转生5次,第八魂技开发进度60%").append(NEWLINE);
        UPDATE_LOG.append("5.17:落日神功 改为=> 落日神弓").append(NEWLINE);
        UPDATE_LOG.append("5.16:修复已知bug,移除武魂 斧头 锄头 蓝银草武魂改为蓝银花 添加武魂 落日神功 九心海棠 其第七魂技也跟随改变;移除神级/百万 年魂环,不可出售的限制,微降选择攻击消耗的魂力").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("5.12:修复已知bug,取消自动选择").append(NEWLINE);
        UPDATE_LOG.append("5.11:修复魂兽死亡之后,仍发送逃跑的信息,修复眩晕偶尔无法打断魂兽蓄力的问题,\'出徒\'").append(NEWLINE);
        UPDATE_LOG.append("5.9 :修复无法选择攻击的bug").append(NEWLINE);
        UPDATE_LOG.append("5.8 :见商城,每周最后的任务奖励升级").append(NEWLINE);
        UPDATE_LOG.append("5.5 :魂兽逃跑时间调整,第八魂技开发进度30%").append(NEWLINE);
        UPDATE_LOG.append("5.3 :修复魂兽反甲无伤害的问题,魂骨属性的增强,优化魂骨显示").append(NEWLINE);
        UPDATE_LOG.append("5.2 :见暗器菜单").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("4.30:修复任务bug").append(NEWLINE);
        UPDATE_LOG.append("4.29:今日已更新").append(NEWLINE);
        UPDATE_LOG.append("4.28:修复bug").append(NEWLINE);
        UPDATE_LOG.append("4.27:修复批量使用精神神石不回复的bug.").append(NEWLINE);
        UPDATE_LOG.append("4.26:修复bug").append(NEWLINE);
        UPDATE_LOG.append("4.25:见魂技更新列表,新物品见商城,新机制2").append(NEWLINE);
        UPDATE_LOG.append("4.24:修复部分bug;平调部分玩家攻击").append(NEWLINE);
        UPDATE_LOG.append("4.24:修复部分bug;移除神王无法获得经验限制,且无经验上限").append(NEWLINE);
        UPDATE_LOG.append("4.23:修复部分bug;https://github.com/gdpl2112/mirai-bot-first/blob/master/to-do.md").append(NEWLINE);
        UPDATE_LOG.append("4.21:修复部分bug").append(NEWLINE);
        UPDATE_LOG.append("4.20:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("4.19:修复魂兽效果异常的bug").append(NEWLINE);
        UPDATE_LOG.append("4.16:修复部分bug,宗门扩增").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("4.13:修复已知bug,微调几率,随机唱鸭").append(NEWLINE);
        UPDATE_LOG.append("4.12:修复已知bug,网易云热评,不可用,第八魂技开发进度8/32").append(NEWLINE);
        UPDATE_LOG.append("4.11:修复已知bug,第八魂技开发进度18%").append(NEWLINE);
        UPDATE_LOG.append("4.10:修复已知bug,第八魂技开发进度15%").append(NEWLINE);
        UPDATE_LOG.append("4.9: 发言排行<n>,今日发言排行<n>,娱乐功能'哪个群友是我老婆\n'").append(NEWLINE);
        UPDATE_LOG.append("4.8: 更新").append(NEWLINE);
        UPDATE_LOG.append("4.5: 新增随机头像功能,见\"娱乐功能\",新功能 疫情<地区>").append(NEWLINE);
        UPDATE_LOG.append("4.5: 修复已知bug,优化回复,修复举牌子,暂时关闭扔瓶子/捡瓶子 功能").append(NEWLINE);
        UPDATE_LOG.append("4.4: 修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("4.3: 修复已知bug,新功能闭关/取消闭关,闭关后不可被攻击和行动").append(NEWLINE);
        UPDATE_LOG.append("4.2: 人性功能( 双修打工进入**").append(NEWLINE);
        UPDATE_LOG.append("4.1: 优化眩晕机制").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("3.31:修复了转生后升级不加属性的bug,在转生一次即可").append(NEWLINE);
        UPDATE_LOG.append("3.30:修复了吸收魂环不加属性的bug").append(NEWLINE);
        UPDATE_LOG.append("3.29:每个等级第一次升级时增加属性").append(NEWLINE);
        UPDATE_LOG.append("3.29:调整魂技,见魂技更新列表,修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("3.28:修复了成语接龙的问题,修复七杀剑武魂真身的bug,周任务的调整,修复免死魂技无效的bug").append(NEWLINE);
        UPDATE_LOG.append("3.27:调整猜拳几率,修复了使用落日花瓣无效的bug,修复了进入星斗可能出现落日森林魂兽的bug,修复了斩杀对魂兽几无效的bug,新增每周最大收益").append(NEWLINE);
        UPDATE_LOG.append("3.26:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("3.25:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("3.25:调整猜拳赢的概率(增加),新魂技见魂技更新列表").append(NEWLINE);
        UPDATE_LOG.append("3.21:重构了代码").append(NEWLINE);
        UPDATE_LOG.append("3.17:见魂技更新列表").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("3.15:修复了已知bug").append(NEWLINE);
        UPDATE_LOG.append("3.15:暗器菜单的更新,修复了任务的bug").append(NEWLINE);
        UPDATE_LOG.append("3.13:限制了物品一次物品 转让/出售 的数量#避免bug").append(NEWLINE);
        UPDATE_LOG.append("3.12:修复了创建宗门失败的bug").append(NEWLINE);
        UPDATE_LOG.append("3.11:更新了代码,新物品:落叶碎片,落日花瓣 详情请见说明").append(NEWLINE);
        UPDATE_LOG.append("3.9:永久护盾最长时间30分钟").append(NEWLINE);
        UPDATE_LOG.append("3.6:修复了暗器问题").append(NEWLINE);
        UPDATE_LOG.append("3.5:修复了不回复的问题").append(NEWLINE);
        UPDATE_LOG.append("3.4: 欢迎访问官网\nhttp://kloping.top\nhttp://kloping.life\n修复了商城无法显示的问题").append(NEWLINE);
        UPDATE_LOG.append("3.2:修复签到/签榜bug").append(NEWLINE);
        UPDATE_LOG.append("3.1:修复已知bug,优化显示'魂环配置'显示").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("2.27:修复已知bug,优化显示,护盾显示于血量之上").append(NEWLINE);
        UPDATE_LOG.append("2.25:移除了精神力低于45时的额外受到伤害,每个武魂自带一点属性(也可能没有,新属性,详情见`我的属性`").append(NEWLINE);
        UPDATE_LOG.append("2.24:修复了暗器显示的bug,修复转生").append(NEWLINE);
        UPDATE_LOG.append("2.21:停用每日的早,中,晚的问候,停用每日零点关闭提示").append(NEWLINE);
        UPDATE_LOG.append("2.20:修复转生后关系还在的bug,修复每天都刷新每周任务的bug").append(NEWLINE);
        UPDATE_LOG.append("2.19:尝试修复信息发不出去的问题,新功能emoji表情合成#发输入法中的两个emoji表情即可").append(NEWLINE);
        UPDATE_LOG.append("2.16:修复部分bug").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("2.12:尽可能的避免了不干净的发言").append(NEWLINE);
        UPDATE_LOG.append("2.11:拼音首字母缩写检测与翻译(私聊").append(NEWLINE);
        UPDATE_LOG.append("2.9 :修复bug").append(NEWLINE);
        UPDATE_LOG.append("2.8 :新增 /搜图<图片> 功能").append(NEWLINE);
        UPDATE_LOG.append("2.8 :修复已知bug,/赞<At>").append(NEWLINE);
        UPDATE_LOG.append("2.7 :啥也没更新").append(NEWLINE);
        UPDATE_LOG.append("2.6 :宵禁功能").append(NEWLINE);
        UPDATE_LOG.append("2.6 :修复已知bug,新功能\"QQ信息\"").append(NEWLINE);
        UPDATE_LOG.append("2.5 :修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("2.2 :新的歌词功能,详情见\"点歌系统\"").append(NEWLINE);
        UPDATE_LOG.append("2.2 :问候语改动,精神攻击削弱").append(NEWLINE);
        UPDATE_LOG.append("2.1 :尝试修复已知Bug").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("1.30:新娱乐功能 /爬<At> /举牌子<msg>").append(NEWLINE);
        UPDATE_LOG.append("1.29:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.28:尝试修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.28:修复已知bug,开放落日森林#存在bug请及时反馈").append(NEWLINE);
        UPDATE_LOG.append("1.27:修复已知bug,新娱乐功能,王者最新皮肤<N>,王者皮肤<名字> #可获取皮肤原画无水印哦 <Face:334>.").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("1.26:修复已知bug,新的周任务,精神力伤害的调整,魂技效果加成的调整").append(NEWLINE);
        UPDATE_LOG.append("1.25:修复已知bug,新年样式#狗头保命").append(NEWLINE);
        UPDATE_LOG.append("1.23: 修复永久加攻击的bug").append(NEWLINE);
        UPDATE_LOG.append("1.23:批量抢劫最大12").append(NEWLINE);
        UPDATE_LOG.append("1.23:介于 精神攻击的 刮痧,对其进行了调整,详情见\"精神力作用\"").append(NEWLINE);
        UPDATE_LOG.append("1.22:新娱乐功能,/滚草<At/图片>").append(NEWLINE);
        UPDATE_LOG.append("1.21:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.20:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("1.18:优化显示,修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.18:魂兽蓄力 时 攻击减少 12%").append(NEWLINE);
        UPDATE_LOG.append("1.17:优化代码 修复 已知bug 一些事件的 调整").append(NEWLINE);
        UPDATE_LOG.append("1.16:调整魂兽生成(等级)").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("1.15:修复已知bug 削弱魂兽 蓄力伤害 削弱生成魂兽").append(NEWLINE);
        UPDATE_LOG.append("1.14:修复已知bug,新娱乐功能: 网易云热评").append(NEWLINE);
        UPDATE_LOG.append("1.12:修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.11-v2:修复已知bug,技能效果可叠加,魂兽的削弱,魂技的更新").append(NEWLINE);
        UPDATE_LOG.append("1.11: 精神攻击<At/#><V> 或 精神攻击<At/#><V> 详情见\"精神力作用\"").append(NEWLINE);
        UPDATE_LOG.append("1.6 : 娱乐功能 变大<-size=value>").append(NEWLINE);
        UPDATE_LOG.append("1.5 : 更新了 更新日志").append(NEWLINE);
        UPDATE_LOG.append("1.3 : 修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("1.2 : 每天0点更新排行,(提升性能").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("12.31: 今年最后一次更新->修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("12.30: 王者语音<名字>(n) 王者图片<名字>").append(NEWLINE);
        UPDATE_LOG.append("12.26: 金魂币消费记录").append(NEWLINE);
        UPDATE_LOG.append("12.26: 修复已知bug").append(NEWLINE);
        UPDATE_LOG.append("12.24: 原神公告<n> 王者公告<n>").append(NEWLINE);
        UPDATE_LOG.append("12.23: 批量抢劫").append(NEWLINE);
        UPDATE_LOG.append("12.22: 催更,开发计划,王者荣耀最新公告/王者公告 ").append(NEWLINE);
        UPDATE_LOG.append("12.21: 修复已知bug 微调 魂兽概率").append(NEWLINE);
        UPDATE_LOG.append("12.20: 修复同时完成任务的bug,新的每周任务").append(NEWLINE);
        UPDATE_LOG.append("12.18: 修复已知bug,百科<名字>").append(NEWLINE);
        UPDATE_LOG.append("12.18: 新增娱乐功能 捡瓶子/捡漂流瓶  扔瓶子/仍漂流瓶<内容>").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("12.16: 修复已知bug, 模糊进入\n\t#进入星斗大森林,进入星斗,进入极北\n- 接每周任务\r\n\t #每周任务在每周早刷新,现只有一个任务,之后会继续更新任务").append(NEWLINE);
        UPDATE_LOG.append("12.14: 修复已知bug,升级第<几>魂环,合成<物品名>\n\t接徒弟任务,当前任务 #测试阶段  ").append(NEWLINE);
        UPDATE_LOG.append("12.13: 修复已知bug,别戳我 ").append(NEWLINE);
        UPDATE_LOG.append("12.11: /丢@xx ").append(NEWLINE);
        UPDATE_LOG.append("12.11: 新物品,(白/黄/紫/黑/红)升级券 获取方式后续更新,降低名师点要求 ").append(NEWLINE);
        UPDATE_LOG.append("12.9 : 中级魂骨可出售,降低名师点要求 ").append(NEWLINE);
        UPDATE_LOG.append("12.6 : 魂技使用次数,降低名师点要求").append(NEWLINE);
        UPDATE_LOG.append("12.5 : 魂技加血/魂力 技能 以自身值为基础 且 不会为 被增益者 回复超过最大值一半的量").append(NEWLINE);
        UPDATE_LOG.append("12.4 : 验证码忽略大小写/可 看不清 ").append(NEWLINE);
        UPDATE_LOG.append("12.3 : 进群验证(开关 (开启/关闭)验证 ").append(NEWLINE);
        UPDATE_LOG.append("12.2 : 详细信息 收徒@xx 出师").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("11.29: /推@ ").append(NEWLINE);
        UPDATE_LOG.append("11.27: 开始成语接龙 (完善 ").append(NEWLINE);
        UPDATE_LOG.append("11.26: 一些 欢迎啥的 ").append(NEWLINE);
        UPDATE_LOG.append("11.25: 看似更新了 其实没更新 ").append(NEWLINE);
        UPDATE_LOG.append("11.23: 修复已知 Bug , 融合关系 转移至 \"关系列表\" ").append(NEWLINE);
        UPDATE_LOG.append("11.22: 测试性 功能 创建分身 ").append(NEWLINE);
        UPDATE_LOG.append("11.20:修复点歌, \"排行\" 改为 \"等级排行\" ").append(NEWLINE);
        UPDATE_LOG.append("11.19: 大无语事件的发生 与 解决 ").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("11.13.19: 修复反甲不能打魂兽的Bug ").append(NEWLINE);
        UPDATE_LOG.append("11.13: 修复已知Bug, 菜单变动 魂兽生成削弱 魂兽锁定条件削弱").append(NEWLINE);
        UPDATE_LOG.append("11.5: 尝试修复魂兽击杀排行的 Bug").append(NEWLINE);
        UPDATE_LOG.append("11.4: 修复注册失败的 Bug").append(NEWLINE);
        UPDATE_LOG.append("11.3: 我的魂技 修复已知Bug").append(NEWLINE);
        UPDATE_LOG.append("11.1: 修复-已知Bug.V2 添加血量为空降级的冷却\n\t降低魂兽匹配实力\n\t选择攻击伤害0.33~0.44提升至0.35~0.48").append(NEWLINE);
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("10.31: 修复 闪照破解\n\t 优化性能 且测试").append(NEWLINE);
        UPDATE_LOG.append("10.27: 修复魂技 异常 和 语音 不全的Bug").append(NEWLINE);
        UPDATE_LOG.append("10.26:开始会话 结束会话").append(NEWLINE);
        UPDATE_LOG.append("10.23: 尝试修复 选择攻击发不出的情况").append(NEWLINE);
        UPDATE_LOG.append("10.21: 解析抖音图片 地址\n\t撤回功能实现\n");
        UPDATE_LOG.append("10.17: 解析快手图片 地址\n");
        UPDATE_LOG.append("[nextPage]").append(NEWLINE);
        UPDATE_LOG.append("10.13: 魂兽击杀排行 修复 显示魂力消耗问题\n");
        UPDATE_LOG.append("10.10:魂力消耗调整,暗器增加伤害上限,暗器制作成本调整,魂技增幅属性调整\n见暗器制作表\n魂技增益性不会超过本身最大值").append("\r\n");
        UPDATE_LOGS = UPDATE_LOG.toString().split("\\[nextPage]\\n");
    }

    public NoticeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("精神力作用")
    public Object more1() {
        return VelocityUtils.getTemplateToString("hj.intro", MAX_SA_LOSE_HJ_B, HJ_LOSE_1_X, MAX_SA_LOSE_HP_B);
    }

    @Action("更新日志.*?")
    public String updateLog(@AllMess String m) {
        Integer i = 0;
        i = Tool.INSTANCE.getInteagerFromStr(m);
        i = i == null || i >= UPDATE_LOGS.length ? 0 : i;
        return UPDATE_LOGS[i];
    }

    @Action("魂环吸收限制")
    public Object more2() {
        return VelocityUtils.getTemplateToString("hh.join.intro.vm", MAX_SA_LOSE_HJ_B, HJ_LOSE_1_X, MAX_SA_LOSE_HP_B);
    }

    @Action(value = ".*?机制.*?", otherName = {".*?规则.*?"})
    public String m1() {
        return VelocityUtils.getTemplateToString("rule.intro.0", UseRestrictions.MAX_MEET_C, LOWST);
    }

    @Action("竞猜说明")
    private String m4() {
        return VelocityUtils.getTemplateToString("quiz.intro.vm");
    }
}
