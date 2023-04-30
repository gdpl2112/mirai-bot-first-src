package Project.aSpring.mcs.controllers;

import Project.commons.SpUser;
import Project.commons.TradingRecord;
import Project.commons.UserScore;
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
import io.github.kloping.mirai0.unitls.Tools.Tool;
import io.github.kloping.url.UrlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.Charset;
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

    @AutoStand
    static ControllerSource controllerSource;

    @Value("${auth.pwd:123456}")
    String pwd0;

    @Value("${auth.super.pwd:123456}")
    String pwd1;
    @Value("${web.url:http://localhost}")
    String webUrl;
    @Value("${web.pwd:123456}")
    String webPwd;

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
            MessageUtils.INSTANCE.sendMessageInOneFromGroup("您当前正在查看记录,若没有请忽略此条消息\r\n您的验证码是:" + code0, Long.parseLong(qid));
            return uuid;
        } else {
            return "err";
        }
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

    @PostMapping("uploadTips")
    public Object uploadTips(@RequestBody String data) {
        try {
            MessageUtils.INSTANCE.sendMessageInGroup("有新的帖子上传成功", CAP_GID);
            data = URLDecoder.decode(data, Charset.forName("UTF-8"));
            if (!data.endsWith("}")) {
                int i = data.lastIndexOf("}");
                data = data.substring(0, i + 1);
            }
            MessageUtils.INSTANCE.sendMessageInGroupI(data, CAP_GID);
            Notice notice = JSON.parseObject(data, Notice.class);
            MessageUtils.INSTANCE.sendMessageInGroupI(JSON.toJSONString(notice), CAP_GID);
            Document doc = Jsoup.parse(notice.getHtml());
            doc.outputSettings().prettyPrint(true);
            String ds = doc.toString();
            MessageUtils.INSTANCE.sendMessageInGroupI(ds, CAP_GID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @GetMapping("say")
    public synchronized String add(@RequestParam("gid") Long gid, @RequestParam("pwd") String pwd, @RequestParam("s") String s) {
        if (!pwd.equals(pwd1)) return "err";
        MessageUtils.INSTANCE.sendMessageInGroup(s, gid);
        return "ok";
    }

    @GetMapping("getUser")
    public UserScore get(@RequestParam("qid") Long qid, @RequestParam("pwd") String pwd) {
        if (!pwd.equals(pwd1)) return null;
        return DataBase.getUserInfo(qid);
    }

    public String accept(Integer id) {
        return UrlUtils.getStringFromHttpUrl(webUrl + "/accept?id=" + id + "&pwd=" + webPwd);
    }
}
