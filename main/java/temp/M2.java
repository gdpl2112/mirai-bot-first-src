package temp;

import Entitys.gameEntitys.task.Task;
import com.madgag.gif.fmsware.GifDecoder;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class M2 {
    public static void main(String[] args) throws Exception {
        Document document = Jsoup.connect("http://123.57.42.227/api/search/song?keyword=%E5%85%B3%E9%94%AE%E8%AF%8D&type=kugou")
                .ignoreContentType(true).get();
        System.out.println(document.body().text());
    }
}
