package io.github.kloping.kzero.aigame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kloping.number.NumberUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import static io.github.kloping.kzero.aigame.GameMain.*;

/**
 * @author github kloping
 * @date 2025/4/18
 */
public class ItemModule {
    public static void load() {
        GameMain.GLOBAL_MATCHES.add(MatchBean.create("背包", (t, s) -> {
            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/items/list?id=" + t.getSender().getId(), String.class);
            JSONArray array = JSON.parseArray(data.getBody());
            StringBuilder sb = new StringBuilder("🎒 背包 🎒\n");
            for (Object o : array) {
                JSONObject jo = (JSONObject) o;
                int n = jo.getInteger("quantity");
                sb.append("🆔").append(jo.getInteger("speciesId")).append(".").append(jo.getString("name"));
                if (n > 1) sb.append("✖️").append(n);
                sb.append("\n");
            }
            toResult(t, sb.toString());
        }));

        GameMain.GLOBAL_MATCHES.add(MatchBean.create("商城", (t, s) -> {
            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/items/shop?id=" + t.getSender().getId(), String.class);
            JSONArray array = JSON.parseArray(data.getBody());
            StringBuilder sb = new StringBuilder("🏪 商城 🏪\n");
            for (Object o : array) {
                JSONObject jo = (JSONObject) o;
                int n = jo.getInteger("count");
                sb.append("🆔 ").append(jo.getInteger("speciesId")).append(".").append(jo.getString("name"));
                sb.append("\t\t💰").append(jo.getString("price")).append("/个").append("\t\t📦剩").append(n).append("\n");
            }
            toResult(t, sb.toString());
        }));

        GameMain.GLOBAL_MATCHES.add(MatchBean.create("购买", "🛒 购买示例'购买1001x2'\n表示为购买2个经验本", (t, s) -> {
            TwoInt result = getTwoInt(s);
            if (result.isPreped()) toResult(t, "❌ 格式错误\n🛒 购买示例'购买1001x2'\n表示为购买2个经验本");

            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/items/buy" +
                    "?id=" + t.getSender().getId() + "&itemId=" + result.id + "&count=" + result.count, String.class);
            toResult(t, data.getBody());

        }));

        GameMain.GLOBAL_MATCHES.add(MatchBean.create("使用", "🎮 使用示例'使用1001x2'\n表示为使用2个经验本(对置顶宠物使用)", (t, s) -> {
            TwoInt result = getTwoInt(s);
            if (result == null) toResult(t, "❌ 格式错误\n🎮 使用示例'使用1001x2'\n表示为使用2个经验本(对置顶宠物使用)");
            ResponseEntity<String> data = TEMPLATE.getForEntity(URL + "/items/use" +
                    "?id=" + t.getSender().getId() + "&itemId=" + result.id + "&count=" + result.count, String.class);
            JSONObject jo = JSON.parseObject(data.getBody());
            if (jo.getBoolean("success")) {
                String field = jo.getString("field");
                JSONObject jo2 = jo.getJSONObject("data");
                String vv = jo2.getString(field);
                if (field.equals("exp")) {
                    StringBuilder sb = new StringBuilder();
                    if (jo2.getBoolean("isChange")) {
                        sb.append("经验: +").append(jo2.getInteger("exp")).append("\n");
                        sb.append("等级: +").append(jo2.getInteger("level")).append("\n");
                        sb.append("血量: +").append(jo2.getInteger("hp")).append("\n");
                        sb.append("攻击: +").append(jo2.getInteger("attack")).append("\n");
                        sb.append("防御: +").append(jo2.getInteger("defense")).append("\n");
                        sb.append("速度: +").append(jo2.getInteger("speed")).append("\n");
                        toResult(t, sb.toString());
                    } else {
                        toResult(t, jo.getString("name") + "+" + vv);
                    }
                } else {
                    toResult(t, jo.getString("name") + vv);
                }
            } else {
                toResult(t, jo.get("data").toString());
            }
        }));
    }

    private static TwoInt getTwoInt(String s) {
        int id;
        int count = 0;
        try {
            String[] split = s.split("x|X");
            count = 1;
            if (split.length == 2) {
                id = Integer.valueOf(split[0]);
                count = Integer.valueOf(split[1]);
            } else {
                id = Integer.valueOf(split[0]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return new TwoInt(id, count);
    }

    private static class TwoInt {
        public final int id;
        public final int count;

        public TwoInt(int id, int count) {
            this.id = id;
            this.count = count;
        }

        public boolean isPreped() {
            return id > 0 && count > 0;
        }
    }
}