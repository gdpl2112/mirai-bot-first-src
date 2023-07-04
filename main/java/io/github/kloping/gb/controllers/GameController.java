package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.entitys.MatherResult;
import io.github.kloping.gb.BotInterface;
import io.github.kloping.gb.MessageContext;
import io.github.kloping.gb.game.e0.GameDataContext;
import io.github.kloping.gb.services.GameService;

@Controller
public class GameController {

    @AutoStand
    ManagerController managerController;

    @Before
    private Object before(MessageContext context, MatherResult result, BotInterface botInterface) throws NoRunException {
        if (!managerController.isOpen(context.getGid(), this.getClass())) throw new NoRunException();
        GameDataContext context0 = service.getContext(context.getSid());
        String ds = result.getActionStr();
        if (isIllegal(context0, ds)) {
            botInterface.onReturn(context, ds, "无状态!");
            throw new NoRunException();
        } else return context0;
    }

    private boolean isIllegal(GameDataContext context0, String actionStr) {
        return false;
    }

    @AutoStand
    GameService service;

    @Action(value = "信息", otherName = {"武魂信息"})
    public String info(String sid) {
        return service.info(sid);
    }

    @Action("修炼")
    public String xl(GameDataContext context) {
        return service.xl(context);
    }

    @Action("升级")
    public String upup(GameDataContext context){
        return service.upup(context);
    }

    @After
    public void after(GameDataContext context) {
        service.apply(context.getPersonInfo());
        service.apply(context.getWhInfo());
    }
}