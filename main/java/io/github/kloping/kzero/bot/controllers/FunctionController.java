package io.github.kloping.kzero.bot.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.main.api.MessagePack;
import io.github.kloping.kzero.spring.dao.FuncData;
import io.github.kloping.kzero.spring.mapper.FuncDataMapper;
import io.github.kloping.rand.RandomUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ftype 1  一言
 *
 * @author github.kloping
 */
@Controller
public class FunctionController {
    @AutoStand
    FuncDataMapper funcDataMapper;

    private static final SimpleDateFormat SF_MM = new SimpleDateFormat("mm");

    /**
     * 返回分钟
     *
     * @return
     */
    public static Integer getMin() {
        return Integer.parseInt(SF_MM.format(new Date()));
    }


    @CronSchedule("3 1 0 * * ? *")
    public void zero() {
        ids.clear();
        yiy = null;
    }
    //data id 2 触发了
    private static final List<Integer> ids = new ArrayList<>();

    QueryWrapper<FuncData> query1 = new QueryWrapper<>();

    @CronSchedule("3 10-50 7-8 * * ? *")
    public void yiyan() {
        if (ids.size() == funcDataMapper.selectCount(query1)) return;
        List<FuncData> list = funcDataMapper.selectList(query1);
        for (FuncData funcData : list) {
            if (ids.contains(funcData.getId().intValue())) continue;
            boolean a = false;
            if (DateUtils.getHour() == 8 && getMin() == 50) {
                a = true;
            } else {
                int r = RandomUtils.RANDOM.nextInt(80);
                a = r <= 1;
            }
            if (!a) continue;
            String yiy = getDayYiyan();
            ids.add(funcData.getId().intValue());
            SubscribeController.broadcastToBot(funcData.getBid(), funcData.getTid(), funcData.getType(), yiy);
        }
    }

    private static RestTemplate template = new RestTemplate();
    private static String yiy;

    private static String getDayYiyan() {
        if (yiy != null) return yiy;
        String json = template.getForObject("https://api.nnxv.cn/api/yiyan.php", String.class);
        JSONObject data = JSON.parseObject(json);
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.getYear()).append("-").append(DateUtils.getMonth()).append("-").append(DateUtils.getDay()).append(":\n")
                .append(data.getString("content"))
                .append("\n----from《").append(data.getString("from")).append("》(").append(data.getString("typeName")).append(")");
        return yiy = sb.toString();
    }

    public final Map<String, Integer> name2fid = new HashMap<>();

    {
        query1.eq("ftype", 1);
        name2fid.put("一言", 1);
    }

    @Action("开启<.+=>name>")
    public Object open(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        name = name.trim();
        if (name2fid.containsKey(name)) {
            FuncData data = new FuncData();
            data.setBid(bot.getId());
            data.setTid(pack.getSubjectId());
            data.setFtype(name2fid.get(name));
            data.setType(pack.getType().name());
            funcDataMapper.insert(data);
            return "开启成功!";
        } else return "未发现功能项";
    }

    @Action("关闭<.+=>name>")
    public Object close(@Param("name") String name, MessagePack pack, KZeroBot bot) {
        name = name.trim();
        if (name2fid.containsKey(name)) {
            QueryWrapper<FuncData> qw = new QueryWrapper<>();
            qw.eq("bid", bot.getId());
            qw.eq("tid", pack.getSubjectId());
            qw.eq("type", pack.getType().name());
            qw.eq("ftype", name2fid.get(name));
            funcDataMapper.delete(qw);
            return "已关闭!";
        } else return "未发现功能项";
    }

    @Action("功能列表")
    public Object list() {
        return name2fid.keySet();
    }
}
