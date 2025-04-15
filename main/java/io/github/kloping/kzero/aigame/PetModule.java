package io.github.kloping.kzero.aigame;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.number.NumberUtils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import static io.github.kloping.kzero.aigame.GameMain.*;

/**
 * @author github kloping
 * @date 2025/4/15-20:49
 */
public class PetModule {

    public static void load() {
        GameMain.GLOBAL_MATCHES.add(MatchBean.create("领取宠物", (t, s) -> {
            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/pets/available?id=" + t.getSender().getId(), String.class);
            selectActionMap.put(t.getSender().getId(), (i) -> {
                if (i == 0) {
                    toResult(t, "取消");
                } else {
                    ResponseEntity<String> out = TEMPLATE.postForEntity(URL + "/pets/claim"
                            , MultiValueMapUtils.of("id", t.getSender().getId(), "n", i), String.class);
                    toResult(t, out.getBody());
                }
            });
            if (data.getStatusCodeValue() == 200) {
                toResult(t, "回复数字以选择\n" + data.getBody());
            } else {
                toResult(t, data.getBody());
            }
        }));
        GameMain.GLOBAL_MATCHES.add(MatchBean.create("我的宠物", MatchBean.MatchType.EXACT_MATCH, null, (t, s) -> {
            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/pets/list?id=" + t.getSender().getId(), String.class);
            if (data.getStatusCodeValue() == 200) {
                JSONArray array = JSON.parseArray(data.getBody());
                StringBuilder sb = new StringBuilder();
                for (Object o : array) {
                    JSONObject jo = (JSONObject) o;
                    int hp = jo.getInteger("hp");
                    int chp = jo.getInteger("currentHp");
                    sb.append(jo.getString("name")).append(" '").append(jo.getString("type")).append("'系 ");
                    sb.append(jo.getInteger("top") == 1 ? "已置顶\n" : "\n");
                    sb.append(jo.getString("level")).append("级 还需")
                            .append(jo.getInteger("requiredExp") - jo.getInteger("experience")).append("点经验升级\n剩余血量:");
                    sb.append(chp).append("/").append(hp).append("(")
                            .append(NumberUtils.toPercent(chp, hp)).append("%)").append("\n\n");
                }
                toResult(t, sb.toString().trim());
            } else {
                toResult(t, data.getBody());
            }

        }));
        GameMain.GLOBAL_MATCHES.add(MatchBean.create("宠物信息", MatchBean.MatchType.STARTS_WITH, null, (m, s) -> {
            ResponseEntity<String> data = null;
            Integer n = null;
            try {
                if (s != null) n = Integer.valueOf(s.trim());
            } catch (NumberFormatException e) {
            }
            if (n == null) {
                data = TEMPLATE.getForEntity(URL + "/pets/info?id=" + m.getSender().getId(), String.class);
            } else {
                data = TEMPLATE.getForEntity(URL + "/pets/info?n=" + n + "&id=" + m.getSender().getId(), String.class);
            }
            if (data.getStatusCodeValue() == 200) {
                JSONObject obj = JSON.parseObject(data.getBody());
                String base64 = obj.getString("data");
                byte[] bytes = Base64.getDecoder().decode(base64);
                obj = obj.getJSONObject("pet");
                StringBuilder sb = new StringBuilder();
                sb.append("🐾 名字: ").append(obj.getString("name")).append("\n");
                sb.append("🔮 类型: ").append(obj.getString("type")).append("\n");
                sb.append("⭐ 等级:").append(obj.getInteger("level")).append("lv\n");
                sb.append("📈 经验:").append(obj.getInteger("experience")).append("/").append(obj.getInteger("requiredExp")).append("\n");
                sb.append(getProgressBar(obj.getInteger("experience"), obj.getInteger("requiredExp"), 10, "⬜", "🟦")).append("\n");
                sb.append("❤️ 血量:").append(obj.getInteger("currentHp")).append("/").append(obj.getInteger("hp")).append("\n");
                sb.append(getProgressBar(obj.getInteger("currentHp"), obj.getInteger("hp"), 10, "⬜", "🟩")).append("\n");
                sb.append("🏃 速度:").append(obj.getInteger("speed")).append("\n");
                sb.append("⚔️ 攻击:").append(obj.getInteger("attack")).append("\n");
                sb.append("🛡️ 防御:").append(obj.getInteger("defense")).append("\n");
                sb.append("🎯 暴率:").append(obj.getInteger("critRate")).append("\n");
                sb.append("💥 暴伤:").append(obj.getInteger("critDamage")).append("\n");
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new QuoteReply(m.getMessage()));
                builder.append(Contact.uploadImage(m.getSubject(), new ByteArrayInputStream(bytes)));
                builder.append(sb.toString().trim());
                m.getSubject().sendMessage(builder.build());
            } else {
                toResult(m, data.getBody());
            }
        }));

    }

    private static String getProgressBar(int current, int total, int length, String emptyChar, String filledChar) {
        int filledLength = (int) Math.round((double) current / total * length);
        filledLength = Math.min(filledLength, length);
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < filledLength; i++) {
            progressBar.append(filledChar);
        }
        for (int i = filledLength; i < length; i++) {
            progressBar.append(emptyChar);
        }
        return progressBar.toString();
    }
}
