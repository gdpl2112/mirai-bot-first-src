package Project.detailPlugin;

import Project.interfaces.http_api.GetPvpQQ;
import com.alibaba.fastjson.JSON;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.MySpringTool.entity.ParamsBuilder;
import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQqCom.Response0;
import io.github.kloping.mirai0.commons.apiEntitys.pvpQqCom.v1.Response1;
import io.github.kloping.number.NumberUtils;
import io.github.kloping.url.UrlUtils;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static io.github.kloping.mirai0.Main.Resource.BOT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.IMG_TAG_STR;

/**
 * @author github-kloping
 */
@Entity
public class GetPvpNews {

    @AutoStand
    private GetPvpQQ getPvpQQ;

    public Response0 m1() {
        String urlStr = UrlUtils.getStringFromHttpUrl("https://game.gtimg.cn/images/yxzj/web201706/js/newsindex.js");
        String[] sss = urlStr.split("\r|\n");
        String source = "web_pc";
        int serviceId = Integer.parseInt(NumberUtils.findNumberFromString(sss[11]));
        int i1 = sss[12].indexOf("\'");
        int i2 = sss[12].lastIndexOf("\'");
        String token = sss[12].substring(i1 + 1, i2);
        int id = Integer.parseInt(NumberUtils.findNumberFromString(sss[13]));
        String sign = makeSign(source, serviceId, id, token);
        Response0 data = getPvpQQ.get(new ParamsBuilder()
                .append("serviceId", String.valueOf(serviceId))
                .append("filter", "channel")
                .append("sortby", "sIdxTime")
                .append("source", "web_pc")
                .append("limit", "12")
                .append("logic", "or")
                .append("typeids", "1")
                .append("chanid", "1762")
                .append("start", "0")
                .append("withtop", "yes")
                .append("exclusiveChannel", "4")
                .append("exclusiveChannelSign", sign)
                .append("time", System.currentTimeMillis() / 1000 + "")
                .build());
        return data;
    }


    public String makeSign(String source, int serviceId, int id, String token) {
        long timestamp = System.currentTimeMillis() / 1000;
        String sign = md5(token + source + serviceId + timestamp);
        return sign;
    }

    public String md5(String data) {
        try {
            byte[] md5 = md5(data.getBytes("utf-8"));
            return toHexString(md5);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public byte[] md5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    private String toHexString(byte[] md5) {
        StringBuilder sb = new StringBuilder();
        for (byte b : md5) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

    public Message getNews(long id, long gid) throws Exception {
        MessageChainBuilder sb = new MessageChainBuilder();
        String ss = getPvpQQ.get0(18, "web_pc", id);
        int i1 = ss.indexOf("var searchObj=");
        int i2 = ss.lastIndexOf(";");
        ss = ss.substring(i1 + "var searchObj=".length(), i2);
        Response1 r1 = JSON.parseObject(ss).toJavaObject(Response1.class);
        Document doc = Jsoup.parse(r1.getMsg().getSContent());
        Elements elements = doc.getElementsByTag("span");
        for (Element e : elements) {
            if (hasImgTag(e)) {
                for (Element img : e.getElementsByTag("img")) {
                    sb.append(MessageTools.instance.createImage(BOT.getGroup(gid), img.attr("src"))).append("\n");
                }
            } else {
                sb.append(e.text()).append("\n");
            }
        }
        return sb.build();
    }

    /**
     * 获取王者荣耀官网公告
     *
     * @param m1  前缀
     * @param id  新闻id
     * @param gid 群id
     * @return
     * @throws Exception
     */
    public Message getNews(String m1, long id, long gid) throws Exception {
        MessageChainBuilder sb = new MessageChainBuilder();
        sb.append(m1);
        String ss = getPvpQQ.get0(18, "web_pc", id);
        int i1 = ss.indexOf("var searchObj=");
        int i2 = ss.lastIndexOf(";");
        ss = ss.substring(i1 + "var searchObj=".length(), i2);
        Response1 r1 = JSON.parseObject(ss).toJavaObject(Response1.class);
        Document doc = Jsoup.parse(r1.getMsg().getSContent());
        Elements elements = doc.getElementsByTag("span");
        String upS0 = "";
        for (Element e : elements) {
            if (hasImgTag(e)) {
                for (Element img : e.getElementsByTag("img")) {
                    String m0 = img.attr("src");
                    if (!m0.equals(upS0)) {
                        sb.append(MessageTools.instance.createImage(BOT.getGroup(gid), m0)).append("\n");
                    }
                    upS0 = m0;
                }
            } else {
                String m0 = e.text();
                if (!m0.equals(upS0)) {
                    sb.append(m0).append("\n");
                }
                upS0 = m0;
            }
        }
        return sb.build();
    }

    private boolean hasImgTag(Element element) {
        if (element.children() != null && element.children().size() > 0) {
            for (Element child : element.children()) {
                if (child.tagName().equals(IMG_TAG_STR)) {
                    return true;
                } else {
                    return hasImgTag(child);
                }
            }
        }
        return false;
    }
}
