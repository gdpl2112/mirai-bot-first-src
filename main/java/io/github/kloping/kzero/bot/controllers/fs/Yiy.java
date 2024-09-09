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
public class Yiy implements Fs {
    @Override
    public Integer ftype() {
        return 1;
    }

    @Override
    public String getName() {
        return "一言";
    }

    @Autowired
    FuncDataMapper funcDataMapper;

    /**
     * 返回分钟
     *
     * @return
     */
    public Integer getMin() {
        return Integer.parseInt(SF_MM.format(new Date()));
    }

    @Scheduled(cron = "3 1 0 * * ?")
    public void zero() {
        ids.clear();
        yiy = null;
    }

    //data id 2 触发了
    private final List<Integer> ids = new ArrayList<>();

    @Scheduled(cron = "3 10-50 7-8 * * ?")
    public void yiyan() {
        synchronized (FunctionController.class) {
            if (ids.size() == funcDataMapper.selectCountByType(ftype())) return;
            List<FuncData> list = funcDataMapper.selectList(ftype());
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
    }

    @Autowired
    RestTemplate template;

    private String yiy;

    private String getDayYiyan() {
        if (yiy != null) return yiy;
        String json = template.getForObject("https://api.nnxv.cn/api/yiyan.php", String.class);
        JSONObject data = JSON.parseObject(json);
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.getYear()).append("-").append(DateUtils.getMonth()).append("-").append(DateUtils.getDay()).append(":\n").append(data.getString("content")).append("\n----from《").append(data.getString("from")).append("》(").append(data.getString("typeName")).append(")");
        return yiy = sb.toString();
    }
}
