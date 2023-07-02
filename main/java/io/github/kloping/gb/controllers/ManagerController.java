package io.github.kloping.gb.controllers;

import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import io.github.kloping.MySpringTool.interfaces.Logger;
import io.github.kloping.gb.MessageContext;
import io.github.kloping.gb.finals.FinalStrings;
import io.github.kloping.gb.spring.dao.Father;
import io.github.kloping.gb.spring.dao.GroupConf;
import io.github.kloping.gb.spring.mapper.FatherMapper;
import io.github.kloping.gb.spring.mapper.GroupConfMapper;

/**
 * @author github-kloping
 * @date 2023-06-30
 */
@Controller
public class ManagerController {

    @AutoStand
    GroupConfMapper groupConfMapper;

    @AutoStand
    FatherMapper fatherMapper;

    public boolean isOpen(String gid, Class cla) {
        return getGroupConf(gid).getOpen();
    }

    public GroupConf getGroupConf(String gid) {
        GroupConf groupConf = groupConfMapper.selectById(gid);
        if (groupConf == null) {
            groupConf = new GroupConf();
            groupConf.setId(gid);
            groupConfMapper.insert(groupConf);
        }
        return groupConf;
    }

    @AutoStand
    Logger logger;

    @Before
    public void before(@AllMess String mess, MessageContext context) throws NoRunException {
        if (isSuperQ(context.getSid())) {
            logger.info("超级权限执行...");
            return;
        } else if (isFather(context)) {
            return;
        }
        throw new NoRunException("无权限");
    }

    private boolean isFather(MessageContext context) {
        Father father = fatherMapper.selectById(context.getSid());
        if (father == null) {
            return false;
        } else {
            if (father.getGids().contains("0")) return true;
            return father.getGids().contains(context.getGid());
        }
    }

    private boolean isSuperQ(String sid) {
        return "7749068863541459083".equals(sid);
    }

    @Action(value = FinalStrings.OPEN_STR, otherName = "说话")
    public String open(MessageContext context) {
        GroupConf conf = getGroupConf(context.getGid());
        conf.setOpen(true);
        groupConfMapper.updateById(conf);
        return "已经开启!";
    }

    @Action(value = FinalStrings.CLOSE_STR, otherName = "闭嘴")
    public String close(MessageContext context) {
        GroupConf conf = getGroupConf(context.getGid());
        conf.setOpen(false);
        groupConfMapper.updateById(conf);
        return "已经关闭!";
    }
}
