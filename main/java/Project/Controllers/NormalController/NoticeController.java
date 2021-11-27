package Project.Controllers.NormalController;

import Entitys.Group;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class NoticeController {
    public NoticeController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    private static final String moraStr1 = "精神力有什么用:\r\n\t" +
            "   1.精神力高于80%时格挡10%的伤害,同时消耗精神力\r\n\t" +
            "   2.精神力低于45%时额外受到10%的伤害\r\n\t" +
            "   3.魂兽也存在精神力,若魂兽的精神力与你精神力差不多,魂兽可凭借精神力隐藏自己的实力,可使用(探查)消耗精神力探查其状态";

    @Action("精神力作用")
    public Object more1() {
        return moraStr1;
    }

    private static final String moraStr2 = "特别的,魂环吸收最低等级限制:\r\n\t" +
            "   9级不能吸收万年及以上\n\t" +
            "   49级不能吸收十万年及以上\n\t" +
            "   69级不能吸收百万年及以上\n\t";

    private static final StringBuilder sb = new StringBuilder();

    static {
        sb.append("11.27: 开始成语接龙 (正在完善 ").append("\n");
        sb.append("11.26: 一些 欢迎啥的 ").append("\n");
        sb.append("11.25:  看似更新了 其实没更新 ").append("\n");
        sb.append("11.23: 修复已知 Bug , 融合关系 转移至 \"关系列表\" ").append("\n");
        sb.append("11.22: 测试性 功能 创建分身 ").append("\n");
        sb.append("11.20:修复点歌, \"排行\" 改为 \"等级排行\" ").append("\n");
        sb.append("11.19: 大无语事件的发生 与 解决 ").append("\n");
        sb.append("11.13.19: 修复反甲不能打魂兽的Bug ").append("\n");
        sb.append("11.13: 修复已知Bug, 菜单变动 魂兽生成削弱 魂兽锁定条件削弱").append("\n");
        sb.append("11.5: 尝试修复魂兽击杀排行的 Bug").append("\n");
        sb.append("11.4: 修复注册失败的 Bug").append("\n");
        sb.append("11.3: 我的魂技 修复已知Bug").append("\n");
        sb.append("11.1: 修复-已知Bug.V2 添加血量为空降级的冷却\n\t降低魂兽匹配实力\n\t选择攻击伤害0.33~0.44提升至0.35~0.48").append("\n");
//        sb.append("10.31: 修复 闪照破解\n\t 优化性能 且测试").append("\n");
//        sb.append("10.27: 修复魂技 异常 和 语音 不全的Bug").append("\n");
//        sb.append("10.26:开始会话 结束会话").append("\n");
//        sb.append("10.23: 尝试修复 选择攻击发不出的情况").append("\n");
//        sb.append("10.21: 解析抖音图片 地址\n\t撤回功能实现\n");
//        sb.append("10.17: 解析快手图片 地址\n");
//        sb.append("10.13: 魂兽击杀排行 修复 显示魂力消耗问题\n");
//        sb.append("10.10:魂力消耗调整,暗器增加伤害上限,暗器制作成本调整,魂技增幅属性调整\n见暗器制作表\n魂技增益性不会超过本身最大值").append("\r\n");
    }

    @Action("更新日志")
    public String UpdateLog() {
        return sb.toString();
    }

    @Action("魂环吸收限制")
    public Object more2() {
        return moraStr2;
    }

    private static final String Str3 = "每个人 今日首次 无状态时(血量为0)>清空经验,\n" +
            "再次 无状态时 下降一级,\n" +
            "之后都 清空经验\n" +
            "===============\n" +
            "每日最多下降一级\n" +
            "整10级(10,20...)时不会下降等级\n" +
            "请保证自己的血量健康";

    @Action("新机制")
    public String m1() {
        return Str3;
    }
}
