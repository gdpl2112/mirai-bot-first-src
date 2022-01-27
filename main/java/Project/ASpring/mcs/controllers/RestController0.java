package Project.ASpring.mcs.controllers;

import io.github.kloping.mirai0.Entitys.TradingRecord;
import Project.Controllers.ControllerSource;
import Project.DataBases.OtherDatabase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.Controllers.ControllerSource.getCode;

/**
 * @author github-kloping
 */
@RestController

@Entity
public class RestController0 {
    private static final Map<String, String> ucap = new ConcurrentHashMap<>();
    public static final Set<String> cans = new LinkedHashSet<>();
    public static final Map<String, String> caping = new ConcurrentHashMap<>();
    private static final Map<String, Integer> capingErr = new ConcurrentHashMap<>();

    @AutoStand
    ControllerSource controllerSource;

    @GetMapping("getCap")
    public String getCap() {
        Object[] o = controllerSource.createCapImage();
        String path = o[2].toString();
        String capCode = o[1].toString();
        String uuid = UUID.randomUUID().toString();
        JSONObject jo = new JSONObject();
        jo.put("uuid", uuid);
        jo.put("src", path);
        ucap.put(uuid, capCode);
        return jo.toString();
    }

    @GetMapping("AuthCap")
    public String authCap(@RequestParam("id") String id, @RequestParam("code") String code, @RequestParam("qid") String qid) {
        if (!ucap.containsKey(id)) {
            return "err";
        }
        if (ucap.get(id).toLowerCase().equals(code.toLowerCase())) {
            String uuid = UUID.randomUUID().toString();
            cans.add(uuid);
            String code0 = getCode();
            caping.put(qid, code0);
            MessageTools.sendMessageInOneFromGroup("您当前正在查看记录,若没有请忽略此条消息\r\n您的验证码是:" + code0, Long.parseLong(qid));
            return uuid;
        } else {
            return "err";
        }
    }


    @GetMapping("AuthCap0")
    public String authCap0(@RequestParam("canId") String canId
            , @RequestParam("code") String code, @RequestParam("qid") String qid) {
        if (!cans.contains(canId)) return "err";
        if (!caping.containsKey(qid)) return "err";
        if (caping.get(qid).toLowerCase().equals(code.toLowerCase())) {
            caping.remove(qid);
            cans.remove(canId);
            List<TradingRecord> records = OtherDatabase.getList(Long.parseLong(qid));
            Collections.sort(records, new Comparator<TradingRecord>() {
                @Override
                public int compare(TradingRecord o1, TradingRecord o2) {
                    return (int) (o2.getTime() - o1.getTime());
                }
            });
            return JSON.toJSONString(records);
        }
        return "err";
    }
}
