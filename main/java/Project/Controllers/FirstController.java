package Project.Controllers;

import Project.Tools.Tool;
import Project.interfaces.GetSongs;
import Project.interfaces.RunCode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class FirstController {
    @AutoStand
    public static GetSongs getSong;

    @AutoStand
    public static RunCode runCode;

    @AutoStand
    public static DefaultKaptcha defaultKaptcha;


    @Action("测试")
    public String a() {
        Object[] os = createCapImage();
        return Tool.pathToImg(os[0].toString());
    }

    public static char[] cs = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static Object[] createCapImage() {
        try {
            char[] chars = new char[4];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = cs[Tool.rand.nextInt(cs.length)];
            }
            String caps = new String(chars);
            BufferedImage bi = defaultKaptcha.createImage(caps);
            File file = new File("./temp/" + UUID.randomUUID() + ".png");
            ImageIO.write(bi, "png", file);
            return new Object[]{file.getAbsolutePath(), caps};
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[]{e.getMessage()};
        }
    }
}
