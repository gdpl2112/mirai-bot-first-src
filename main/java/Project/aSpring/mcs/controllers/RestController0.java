package Project.aSpring.mcs.controllers;

import Project.controllers.auto.ControllerSource;
import Project.dataBases.DataBase;
import Project.dataBases.OtherDatabase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.ITools.MemberTools;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.TradingRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.getCode;

/**
 * @author github-kloping
 */
@RestController
@Entity
public class RestController0 {
    public static final Set<String> CANS = new LinkedHashSet<>();
    public static final Map<String, String> CAPING = new ConcurrentHashMap<>();
    private static final Map<String, String> UCAP = new ConcurrentHashMap<>();
    private static final Map<String, Integer> CAPING_ERR = new ConcurrentHashMap<>();

    @AutoStand
    static ControllerSource controllerSource;

    @Value("${auth.pwd:123456}")
    String pwd0;

    @Value("${auth.super.pwd:123456}")
    String pwd1;

    @GetMapping("getCap")
    public String getCap() {
        Object[] o = controllerSource.createCapImage();
        String path = o[2].toString();
        String capCode = o[1].toString();
        String uuid = UUID.randomUUID().toString();
        JSONObject jo = new JSONObject();
        jo.put("uuid", uuid);
        jo.put("src", path);
        UCAP.put(uuid, capCode);
        return jo.toString();
    }

    @GetMapping("AuthCap")
    public String authCap(@RequestParam("id") String id, @RequestParam("code") String code, @RequestParam("qid") String qid) {
        if (!UCAP.containsKey(id)) {
            return "err";
        }
        if (UCAP.get(id).toLowerCase().equals(code.toLowerCase())) {
            String uuid = UUID.randomUUID().toString();
            CANS.add(uuid);
            String code0 = getCode();
            CAPING.put(qid, code0);
            MessageTools.instance.sendMessageInOneFromGroup("您当前正在查看记录,若没有请忽略此条消息\r\n您的验证码是:" + code0, Long.parseLong(qid));
            return uuid;
        } else {
            return "err";
        }
    }

    @GetMapping("authorization0")
    public String authorization0(@RequestParam("pwd") String pwd,
            @RequestParam("qid") Long qid) {
        if (pwd.equals(pwd0)) {
            String code0 = getCode();
            MessageTools.instance.sendMessageInOneFromGroup("您当前正在评论,若没有请忽略此条消息\r\n您的验证码是:" + code0, qid);
            return code0;
        } else return "-1";
    }

    @GetMapping("AuthCap0")
    public String authCap0(@RequestParam("canId") String canId
            , @RequestParam("code") String code, @RequestParam("qid") String qid) {
        if (!CANS.contains(canId)) return "err";
        if (!CAPING.containsKey(qid)) return "err";
        if (CAPING.get(qid).toLowerCase().equals(code.toLowerCase())) {
            CAPING.remove(qid);
            CANS.remove(canId);
            List<TradingRecord> records = OtherDatabase.getList(Long.parseLong(qid));
            return JSON.toJSONString(records);
        }
        return "err";
    }

    @GetMapping("getName")
    public String name(@RequestParam("qid") Long qid) {
        return MemberTools.getName(qid);
    }

    @GetMapping("addScore")
    public synchronized String add(@RequestParam("qid") Long qid, @RequestParam("pwd") String pwd, @RequestParam("s") Long score) {
        if (!pwd.equals(pwd1)) {
            return "err";
        }
        DataBase.addScore(score, qid);
        return "ok";
    }
}
