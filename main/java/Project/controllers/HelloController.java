package Project.controllers;

import Project.commons.SpGroup;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.THREADS;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github.kloping
 */
@Controller
public class HelloController {
    public HelloController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(@AllMess String mess, SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }


    @Action("早.*?")
    public void morning(long q, SpGroup group) {
        if (Tool.INSTANCE.RANDOM.nextInt(10) > 5) return;
        String tips;
        int hour = Tool.INSTANCE.getHour();
        if (hour > 0 && hour <= 4) tips = Tool.INSTANCE.getRandString("深夜了,注意休息", "一点都不早啊,更适合睡觉", "啊?");
        else if (hour <= 5) tips = Tool.INSTANCE.getRandString("好早哦.", "真早啊!", "很早诶");
        else if (hour <= 7) tips = Tool.INSTANCE.getRandString("早上好哦", "早呀", "早安");
        else if (hour <= 9) tips = Tool.INSTANCE.getRandString("早!", "早");
        else if (hour <= 11) tips = Tool.INSTANCE.getRandString("不早了呢~", "上午好奥");
        else if (hour <= 12) tips = Tool.INSTANCE.getRandString("已经中午啦", "中午好", "午");
        else if (hour <= 17) tips = Tool.INSTANCE.getRandString("?下午好", "笨蛋!已经下午啦", "早?");
        else if (hour <= 21) tips = Tool.INSTANCE.getRandString("可是已经晚上了￣▽￣", "晚上啦", "???不早");
        else if (hour <= 23) tips = Tool.INSTANCE.getRandString("已经很晚了,注意身体哦", "快睡觉啦", "(╯﹏╰)晚上好");
        else tips = "";
        THREADS.submit(() -> {
            try {
                Thread.sleep(Tool.INSTANCE.randA(3, 6) * 1000);
                MessageUtils.INSTANCE.sendMessageInGroupWithAt(tips, group.getId(), q);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
