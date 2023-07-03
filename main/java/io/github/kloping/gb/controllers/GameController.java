package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Before;
import io.github.kloping.MySpringTool.annotations.Controller;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.gb.MessageContext;
import io.github.kloping.gb.services.GameService;

@Controller
public class GameController {

    @AutoStand
    ManagerController managerController;

    @Before
    private void before(MessageContext context) throws NoRunException {
        if (!managerController.isOpen(context.getGid(), this.getClass())) throw new NoRunException();
    }

    @AutoStand
    GameService service;

    @Action(value = "信息", otherName = {"武魂信息"})
    public String info(String sid) {
        return service.info(sid);
    }

    @Action("修炼")
    public String xl(String sid) {
        return service.xl(sid);
    }
}