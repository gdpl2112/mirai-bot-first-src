package Project.controllers.auto;

import Project.listeners.EmojiCompositeListenerHost;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static io.github.kloping.mirai0.unitls.Tools.Tool.rand;

/**
 * this is a utils for static class
 *
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class ControllerSource {
    @AutoStand
    public static ControllerSource INSTANCE;

    @AutoStand
    public DefaultKaptcha defaultKaptcha;

    @AutoStand
    public static EmojiCompositeListenerHost emojiCompositeListenerHost;

    public static char[] cs = new char[]{
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static String getCode() {
        char[] chars = new char[4];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = cs[rand.nextInt(cs.length)];
        }
        String caps = new String(chars);
        return caps;
    }

    public Object[] createCapImage() {
        try {
            String caps = getCode();
            BufferedImage bi = defaultKaptcha.createImage(caps);
            File file = new File("./temp/" + UUID.randomUUID() + ".png");
            ImageIO.write(bi, "png", file);
            return new Object[]{file.getAbsolutePath(), caps, file.getName()};
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[]{e.getMessage()};
        }
    }
}
