package Project.detailPlugin;

import Project.interfaces.http_api.BaiKeBaidu;
import Project.interfaces.http_api.Empty;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import net.mamoe.mirai.message.data.PlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;
import static io.github.kloping.mirai0.unitls.Tools.Tool.pathToImg;

/**
 * @author github.kloping
 */
@Entity
public class BaiduBaiKe {
    public static final String HAS_SELECT_CLASS_TAG = "list-dot";
    public static final String MAIN_TAG = "main-content J-content";
    public static final String BAIKE_TITLE_NAME_CLASS_TAG = "lemmaWgt-lemmaTitle lemmaWgt-lemmaTitle-";
    public static final String BAIKE_CONTENT_NAME_CLASS_TAG = "lemma-summary";
    public static final String BAIKE_TITLE_LEVE2_NAME_CLASS_TAG = "J-chapter";
    public static final String BAIKE_TITLE_LEVE2_CLASS_TAG = "title-text";
    public static final String IMG_TAG = "img";
    public static final String IMG_STC_TAG = "src";
    public static final int MAX_SIZE = 3420;
    public static final int MAX_SIZE0 = 3020;
    @AutoStand
    BaiKeBaidu baiKeBaidu;
    @AutoStand
    Empty empty;

    public Object getBaiKe(String name) {
        Document document = baiKeBaidu.doc(name);
        Elements elements = document.getElementsByClass(HAS_SELECT_CLASS_TAG);
        Elements elements0 = document.getElementsByClass(BAIKE_TITLE_LEVE2_NAME_CLASS_TAG);
        if (elements.size() > 0 && elements0.size() == 0) {
            String url = elements.get(0).getElementsByTag("a").get(0).attr("href");
            url = "https://baike.baidu.com" + url;
            document = empty.empty(url);
        }
        return build(document.getElementsByClass("J-content").get(0));
    }

    private Object build(Element element) {
        List obs = new ArrayList();
        int size = 0;
        String s1 = element.getElementsByClass(BAIKE_TITLE_NAME_CLASS_TAG).get(0).getElementsByTag("span").get(0).text();
        obs.add(new PlainText(s1));
        size += s1.length();
        Elements elements = element.getElementsByClass("para");
        List<String> images = new ArrayList<>();
        for (Element e0 : elements) {
            Element e1 = e0.previousElementSibling();
            if (e1 == null) continue;
            String s0 = e1.text() + NEWLINE + e0.text();
            if (size + s0.length() >= MAX_SIZE) break;
            size += s0.length();
            obs.add(new PlainText(s0));
            for (Element img : e0.getElementsByTag(IMG_TAG)) {
                String is = img.attr(IMG_STC_TAG);
                images.add(is);
            }
        }
        obs.add("相关图片:");
        for (String image : images) {
            if (image.isEmpty()) continue;
            obs.add(pathToImg(image));
        }
        return obs.toArray();
    }
}
