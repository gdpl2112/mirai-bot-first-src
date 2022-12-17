package Project.detailPlugin;

import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.apiEntitys.pvpqq.Heroes;
import io.github.kloping.mirai0.commons.apiEntitys.pvpqq.YzzYxs;
import io.github.kloping.mirai0.commons.apiEntitys.pvpqq.pvpQQVoice.Dqpfyy5403;
import io.github.kloping.mirai0.commons.apiEntitys.pvpqq.pvpQQVoice.HeroVoice;
import io.github.kloping.mirai0.unitls.Tools.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.HTTPS_PRE;


/**
 * @author github-kloping
 * @version 1.0
 * @date 2021/12/30-11
 */
@Entity
public class PvpQq {
    public static final Map<String, Dqpfyy5403> NAME2VOICE = new ConcurrentHashMap<>();
    public static final Map<String, Integer> NAME2ID = new ConcurrentHashMap<>();
    public static final Map<Integer, String> ID2NAME = new ConcurrentHashMap<>();
    public static Heroes ALL;

    @AutoStand
    static Project.interfaces.http_api.PvpQq pvpQq;

    public Object getSkinPic(String arg) {
        List list = new LinkedList();
        try {
            Document document = Jsoup.connect(arg).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.69")
                    .get();
            String picStr = document.getElementsByClass("banner").get(0).getElementsByTag("img").get(0).attr("src");
            picStr = HTTPS_PRE + picStr;
            list.add("皮肤原画:" + Tool.tool.pathToImg(picStr));
            try {
                Elements elements = document.getElementsByClass("relation-cont");
                list.add("技能效果");
                Elements es;
                Element e;
                e = elements.get(elements.size() - 3);
                es = e.getElementsByTag("img");
                list.add(Tool.tool.pathToImg(HTTPS_PRE + es.get(0).attr("src")));
                e = elements.get(elements.size() - 2);
                es = e.getElementsByTag("img");
                list.add(Tool.tool.pathToImg(HTTPS_PRE + es.get(0).attr("src")));
                e = elements.get(elements.size() - 1);
                es = e.getElementsByTag("img");
                list.add(Tool.tool.pathToImg(HTTPS_PRE + es.get(0).attr("src")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return list.toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    public static synchronized void m1() {
        if (ALL == null) {
            ALL = pvpQq.heroList();
            ID2NAME.clear();
            NAME2ID.clear();
        }
    }

    public static void m0() {
        if (ALL == null) m1();
        for (YzzYxs yzzYxs : ALL.getYzzyxs4880()) {
            String name = yzzYxs.getYzzyxm4588();
            String sName = yzzYxs.getYzzyxc4613();
            Integer id = Integer.valueOf(yzzYxs.getYzzyxi2602());
            ID2NAME.put(id, name);
            NAME2ID.put(name, id);
            try {
                HeroVoice voice = pvpQq.voice(id.toString());
                if (voice == null) continue;
                if (voice.isArray()) {
                    for (Dqpfyy5403 dqpfyy5403 : voice.getArr()) {
                        String kName = dqpfyy5403.getPfmczt7754();
                        if (sName.trim().equals(kName.trim())) {
                            NAME2VOICE.put(name, dqpfyy5403);
                        } else {
                            NAME2VOICE.put(name + kName, dqpfyy5403);
                        }
                    }
                } else {
                    NAME2VOICE.put(name, voice.getObj());
                }
            } catch (Exception e) {
                System.err.println(String.format("%s(%s)语音获取失败",name,id));
            }
        }
        System.out.println("英雄语音加载完成");
    }

    static {
        Resource.START_AFTER.add(() -> {
            Public.EXECUTOR_SERVICE.submit(() -> {
                m0();
            });
        });
    }

    public Dqpfyy5403 getY4e(String name) {
        if (NAME2VOICE.isEmpty()) {
            return null;
        }
        if (NAME2VOICE.containsKey(name)) {
            return NAME2VOICE.get(name);
        } else {
            return null;
        }
    }
}
