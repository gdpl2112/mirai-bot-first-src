package io.github.kloping.kzero.bot.controllers.fs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.bot.controllers.FunctionController;
import io.github.kloping.kzero.bot.controllers.SubscribeController;
import io.github.kloping.kzero.spring.dao.FuncData;
import io.github.kloping.kzero.spring.mapper.FuncDataMapper;
import io.github.kloping.rand.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author github.kloping
 */
@Component
public class Hots implements Fs {
    @Override
    public String getName() {
        return "热榜";
    }

    @Override
    public Integer ftype() {
        return 2;
    }

    /**
     * 返回分钟
     *
     * @return
     */
    public static Integer getMin() {
        return Integer.parseInt(SF_MM.format(new Date()));
    }

    @Autowired
    FuncDataMapper funcDataMapper;

    //data id 2 触发了
    private final List<Integer> ids = new ArrayList<>();

    @Scheduled(cron = "0 0 0 * * ?")
    public void zero() {
        ids.clear();
    }

    @Scheduled(cron = "3 30-50 18 * * ?")
//    @Scheduled(cron = "50 33 13 * * ?")
    public void hots() {
        synchronized (FunctionController.class) {
            if (ids.size() == funcDataMapper.selectCountByType(ftype())) return;
            List<FuncData> list = funcDataMapper.selectList(ftype());
            for (FuncData funcData : list) {
                if (ids.contains(funcData.getId().intValue())) continue;
                boolean a = false;
                if (DateUtils.getHour() == 18 && getMin() == 50) {
                    a = true;
                } else {
                    int r = RandomUtils.RANDOM.nextInt(20);
                    a = r <= 1;
                }
                if (!a) continue;

                String[] contents = this.getContents();
                ids.add(funcData.getId().intValue());
                for (String content : contents) {
                    SubscribeController.broadcastToBot(funcData.getBid(), funcData.getTid(), funcData.getType(), content);
                }
            }
        }
    }

    public static final String URL = "https://api.lolimi.cn/API/jhrb/?hot=";

    @Autowired
    RestTemplate template;

    private String[] getContents() {
        String json = template.getForObject(URL + "快手", String.class);
        JSONObject jo = JSON.parseObject(json);
        StringBuilder sb0 = new StringBuilder();
        int index = 1;
        for (Object object : jo.getJSONArray("data")) {
            JSONObject jo1 = (JSONObject) object;
            sb0.append(index).append(".[").append(jo1.getString("title")).append("]").append(jo1.getString("hot")).append("🔥\n");
            index++;
            if (index == 10) break;
        }

        json = template.getForObject(URL + "抖音", String.class);
        jo = JSON.parseObject(json);
        StringBuilder sb1 = new StringBuilder();
        index = 1;
        for (Object object : jo.getJSONArray("data")) {
            if (index == 10) break;
            JSONObject jo1 = (JSONObject) object;
            sb1.append(index).append(".[").append(jo1.getString("title")).append("]").append(jo1.getString("hot")).append("🔥\n");
            index++;
        }
        return new String[]{sb0.toString().trim(), sb1.toString().trim()};
    }
}