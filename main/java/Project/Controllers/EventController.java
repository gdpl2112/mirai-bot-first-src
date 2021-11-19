package Project.Controllers;

import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import static Project.Controllers.ControllerTool.CanGroup;
import static io.github.kloping.Mirai.Main.Resource.Switch.AllK;
import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class EventController {
    public EventController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void Before(Group group, Member member) {
        if (!AllK)
            throw new NoRunException();
        if (member.getId() != group.getOwner().getId())
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }
}