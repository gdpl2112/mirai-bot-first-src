package Project.aSpring.mcs.controllers;

import Project.controllers.auto.ControllerSource;
import Project.dataBases.DataBase;
import Project.dataBases.OtherDatabase;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.Main.iutils.MemberUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.commons.Notice;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.mirai0.commons.TradingRecord;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.url.UrlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Project.controllers.auto.ControllerSource.getCode;
import static io.github.kloping.mirai0.Main.BootstarpResource.CAP_GID;

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

    @GetMapping("authCap1")
    public String authCap(@RequestParam("id") String id, @RequestParam("code") String code) {
        if (!UCAP.containsKey(id)) {
            return "err";
        }
        if (UCAP.get(id).toLowerCase().equals(code.toLowerCase())) {
            UCAP.remove(id);
            return "ok";
        } else {
            return "err";
        }
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
            MessageUtils.INSTANCE.sendMessageInOneFromGroup("您当前正在查看记录,若没有请忽略此条消息\r\n您的验证码是:" + code0, Long.parseLong(qid));
            return uuid;
        } else {
            return "err";
        }
    }

    @GetMapping("authorization0")
    public String authorization0(@RequestParam("pwd") String pwd, @RequestParam("qid") Long qid) {
        if (pwd.equals(pwd0)) {
            String code0 = getCode();
            MessageUtils.INSTANCE.sendMessageInOneFromGroup("您当前正在评论,若没有请忽略此条消息\r\n您的验证码是:" + code0, qid);
            return code0;
        } else return "-1";
    }

    @GetMapping("requestCode0")
    public Object requestCode0(@RequestParam("pwd") String pwd, @RequestParam("qid") Long qid) {
        if (pwd.equals(pwd0)) {
            SpUser spUser = MemberUtils.getUser(qid);
            if (spUser == null) return "0";
            Integer code0 = Tool.INSTANCE.RANDOM.nextInt(900000) + 10000;
            MessageUtils.INSTANCE.sendMessageInOneFromGroup("您当前正在的登录,若没有请忽略此条消息\r\n您的验证码是:" + code0, qid);
            return code0;
        } else {
            return "-1";
        }
    }

    @GetMapping("uploadTips")
    public Object uploadTips(@RequestParam("data") String data) {
        Notice notice = JSON.parseObject(data, Notice.class);
        MessageUtils.INSTANCE.sendMessageInGroup("有新的帖子上传成功", CAP_GID);
        MessageUtils.INSTANCE.sendMessageInGroupI(JSON.toJSONString(notice), CAP_GID);
        Document doc = Jsoup.parse(notice.getHtml());
        doc.outputSettings().prettyPrint(true);
        String ds = doc.toString();
        MessageUtils.INSTANCE.sendMessageInGroupI(ds, CAP_GID);
        return "ok";
    }

    @GetMapping("AuthCap0")
    public String authCap0(@RequestParam("canId") String canId, @RequestParam("code") String code, @RequestParam("qid") String qid) {
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
        return MemberUtils.getName(qid);
    }

    @GetMapping("addScore")
    public synchronized String add(@RequestParam("qid") Long qid, @RequestParam("pwd") String pwd, @RequestParam("s") Long score) {
        if (!pwd.equals(pwd1)) return "err";
        DataBase.addScore(score, qid);
        return "ok";
    }

    @Value("${web.url:http://localhost}")
    String webUrl;

    @Value("${web.pwd:123456}")
    String webPwd;

    public String accept(Integer id) {
       return UrlUtils.getStringFromHttpUrl(webUrl + "/accept?id=" + id + "&pwd=" + webPwd);
    }
}
