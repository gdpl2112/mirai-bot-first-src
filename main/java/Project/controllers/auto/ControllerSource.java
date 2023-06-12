package Project.controllers.auto;

import Project.commons.apiEntitys.RunnableWithOver;
import Project.controllers.FirstController;
import Project.controllers.gameControllers.GameController2;
import Project.controllers.normalController.SessionController;
import Project.controllers.normalController.SummonPicController;
import Project.interfaces.httpApi.KlopingWeb;
import Project.listeners.EmojiCompositeListenerHost;
import Project.plugins.AiBaiduDetail;
import Project.recivers.GameReceiver0;
import Project.recivers.RewardReceiver;
import Project.services.detailServices.ChallengeDetailService;
import Project.services.impl.GameServiceImpl;
import Project.services.impl.ZongMenServiceImpl;
import Project.services.player.PlayerBehavioralManager;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.gson.Gson;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;


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
    public static AiBaiduDetail aiBaiduDetail;
    @AutoStand
    public static EmojiCompositeListenerHost emojiCompositeListenerHost;
    @AutoStand
    public static ChallengeDetailService challengeDetailService;
    @AutoStand
    public static SummonPicController entertainmentController3;
    @AutoStand
    public static PlayerBehavioralManager playerBehavioralManager;
    @AutoStand
    public static GameServiceImpl gameService;
    @AutoStand
    public static FirstController firstController;
    @AutoStand
    public static SessionController sessionController;

    @AutoStand
    public static GameController2 gameController2;

    @AutoStand
    public static KlopingWeb klopingWeb;

    @AutoStand
    public static ZongMenServiceImpl zongMenService;

    @AutoStand(id = "m100")
    public static Set<RunnableWithOver> m100;

    @AutoStand(id = "m3000")
    public static Set<RunnableWithOver> m3000;

    @AutoStand(id = "gson0")
    public static Gson gson0;

    public static GameReceiver0 gameReceiver0 = new GameReceiver0();
    public static RewardReceiver rewardReceiver = new RewardReceiver();

    public static char[] cs = new char[]{
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    @AutoStand
    public static DefaultKaptcha defaultKaptcha;

    public static String getCode() {
        char[] chars = new char[4];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = cs[Tool.INSTANCE.RANDOM.nextInt(cs.length)];
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
