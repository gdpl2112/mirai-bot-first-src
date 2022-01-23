package Project.Controllers;

import Project.interfaces.*;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.github.kloping.MySpringTool.StarterApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static Project.Tools.Tool.rand;

/**
 * this is a utils for static class
 *
 * @author github-kloping
 * @version 1.0
 */
public class ControllerSource {

    public static final GetSongs GET_SONGS =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(GetSongs.class);

    public static RunCode runCode =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(RunCode.class);

    public static DefaultKaptcha defaultKaptcha =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(DefaultKaptcha.class);

    public static GetPvpQQ getPvpQQ =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(GetPvpQQ.class);

    public static ApiIyk0 apiIyk0 =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(ApiIyk0.class);

    public static MuXiaoGuo muXiaoGuo =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(MuXiaoGuo.class);

    public static Mihoyo mihoyo =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(Mihoyo.class);

    public static PvpQq pvpQq =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(PvpQq.class);

    public static SearchPics searchPics =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(SearchPics.class);

    public static WeatherI weatherM =
            StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(WeatherI.class);

    public static char[] cs = new char[]{
            '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static String getCode() {
        char[] chars = new char[4];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = cs[rand.nextInt(cs.length)];
        }
        String caps = new String(chars);
        return caps;
    }

    public static Object[] createCapImage() {
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
