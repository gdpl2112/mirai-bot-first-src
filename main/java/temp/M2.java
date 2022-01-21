package temp;

import Entitys.gameEntitys.task.Task;
import com.madgag.gif.fmsware.GifDecoder;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class M2 {
    public static void main(String[] args) throws Exception {
        GifDecoder decoder = new GifDecoder();
        decoder.read("D:\\Projects\\OwnProjects\\MyMirai_01\\data\\m0\\m0.gif");
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage bi = decoder.getFrame(i);
            ImageIO.write(bi, "png",
                    new File("D:\\Projects\\OwnProjects\\MyMirai_01\\data\\m0\\m" + i + ".png"));
        }
    }
}
