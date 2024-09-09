package io.github.kloping.kzero.bot.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.kzero.bot.controllers.fs.Fs;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.FuncData;
import io.github.kloping.kzero.spring.mapper.FuncDataMapper;
import io.github.kloping.spt.annotations.Action;
import io.github.kloping.spt.annotations.AutoStand;
import io.github.kloping.spt.annotations.Controller;
import io.github.kloping.spt.annotations.Param;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ftype 1  一言
 *
 * @author github.kloping
 */
@Controller
public class FunctionController {
    @AutoStand
    FuncDataMapper funcDataMapper;

    @AutoStand
    ConfigurableApplicationContext context;

    @Action("开启<.+=>name>")
    public Object open(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        name = name.trim();
        for (String id : context.getBeanNamesForType(Fs.class)) {
            Fs fs = (Fs) context.getBean(id);
            if (fs.getName().equals(name)) {
                FuncData data = new FuncData();
                data.setBid(bot.getId());
                data.setTid(pack.getSubjectId());
                data.setFtype(fs.ftype());
                data.setType(pack.getType().name());
                funcDataMapper.insert(data);
                return "已开启!";
            }
        }
        return "未发现功能项";
    }

    @Action("关闭<.+=>name>")
    public Object close(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        name = name.trim();
        for (String id : context.getBeanNamesForType(Fs.class)) {
            Fs fs = (Fs) context.getBean(id);
            if (fs.getName().equals(name)) {
                QueryWrapper<FuncData> qw = new QueryWrapper<>();
                qw.eq("bid", bot.getId());
                qw.eq("tid", pack.getSubjectId());
                qw.eq("type", pack.getType().name());
                qw.eq("ftype", fs.ftype());
                funcDataMapper.delete(qw);
                return "已关闭!";
            }
        }
        return "未发现功能项";
    }

    @Action("功能列表")
    public Object list() {
        StringBuilder builder = new StringBuilder();
        for (String id : context.getBeanNamesForType(Fs.class)) {
            Fs fs = (Fs) context.getBean(id);
            builder.append(fs.ftype()).append(".").append(fs.getName()).append("\n");
        }
        return builder.toString().trim();
    }
}
