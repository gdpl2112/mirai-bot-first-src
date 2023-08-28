package Project.listeners;

import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai.MessageSerializer;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import Project.utils.Tools.Tool;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
@Entity
public class EmojiCompositeListenerHost {

    public static final Map<Integer, String> DATA_MAPPING = new HashMap<>();
    private static final String P0 = "[\\uD83C\\uDC00-\\uD83E\\uDE4F]{2}";
    /**
     * https://www.gstatic.com/android/keyboard/emojikitchen/20201001/u1f601/u1f601_u1f60c.png
     */
    private static final String U0 = "https://www.gstatic.com/android/keyboard/emojikitchen/%s/u%s/u%s_u%s.png";
    private static Character MIN_LOW_SURROGATE = '\uDC00';
    private static Character MIN_HIGH_SURROGATE = '\uD800';
    private static int MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;
    private static int MAX_CODE_POINT = 0X10FFFF;

    static {
        DATA_MAPPING.put(128516, "20201001");
        DATA_MAPPING.put(128512, "20201001");
        DATA_MAPPING.put(128578, "20201001");
        DATA_MAPPING.put(128579, "20201001");
        DATA_MAPPING.put(128521, "20201001");
        DATA_MAPPING.put(128522, "20201001");
        DATA_MAPPING.put(128518, "20201001");
        DATA_MAPPING.put(128515, "20201001");
        DATA_MAPPING.put(128513, "20201001");
        DATA_MAPPING.put(129315, "20201001");
        DATA_MAPPING.put(128517, "20201001");
        DATA_MAPPING.put(128514, "20201001");
        DATA_MAPPING.put(128519, "20201001");
        DATA_MAPPING.put(129392, "20201001");
        DATA_MAPPING.put(128525, "20201001");
        DATA_MAPPING.put(128536, "20201001");
        DATA_MAPPING.put(129321, "20201001");
        DATA_MAPPING.put(128535, "20201001");
        DATA_MAPPING.put(128538, "20201001");
        DATA_MAPPING.put(128537, "20201001");
        DATA_MAPPING.put(128539, "20201001");
        DATA_MAPPING.put(128541, "20201001");
        DATA_MAPPING.put(128523, "20201001");
        DATA_MAPPING.put(129394, "20201001");
        DATA_MAPPING.put(129297, "20201001");
        DATA_MAPPING.put(128540, "20201001");
        DATA_MAPPING.put(129303, "20201001");
        DATA_MAPPING.put(129323, "20201001");
        DATA_MAPPING.put(129300, "20201001");
        DATA_MAPPING.put(129325, "20201001");
        DATA_MAPPING.put(129320, "20201001");
        DATA_MAPPING.put(129296, "20201001");
        DATA_MAPPING.put(128528, "20201001");
        DATA_MAPPING.put(128529, "20201001");
        DATA_MAPPING.put(128566, "20201001");
        DATA_MAPPING.put(129322, "20201001");
        DATA_MAPPING.put(128566, "20210218");
        DATA_MAPPING.put(8205, "20210218");
        DATA_MAPPING.put(127787, "20210218");
        DATA_MAPPING.put(65039, "20210218");
        DATA_MAPPING.put(128527, "20201001");
        DATA_MAPPING.put(128530, "20201001");
        DATA_MAPPING.put(128580, "20201001");
        DATA_MAPPING.put(128556, "20201001");
        DATA_MAPPING.put(128558, "20210218");
        DATA_MAPPING.put(8205, "20210218");
        DATA_MAPPING.put(128168, "20210218");
        DATA_MAPPING.put(129317, "20201001");
        DATA_MAPPING.put(128524, "20201001");
        DATA_MAPPING.put(128532, "20201001");
        DATA_MAPPING.put(128554, "20201001");
        DATA_MAPPING.put(129316, "20201001");
        DATA_MAPPING.put(128564, "20201001");
        DATA_MAPPING.put(128567, "20201001");
        DATA_MAPPING.put(129298, "20201001");
        DATA_MAPPING.put(129301, "20201001");
        DATA_MAPPING.put(129314, "20201001");
        DATA_MAPPING.put(129326, "20201001");
        DATA_MAPPING.put(129319, "20201001");
        DATA_MAPPING.put(129397, "20201001");
        DATA_MAPPING.put(129398, "20201001");
        DATA_MAPPING.put(128565, "20201001");
        DATA_MAPPING.put(129396, "20201001");
        DATA_MAPPING.put(129760, "20211115");
        DATA_MAPPING.put(129327, "20201001");
        DATA_MAPPING.put(129312, "20201001");
        DATA_MAPPING.put(129395, "20201001");
        DATA_MAPPING.put(129400, "20201001");
        DATA_MAPPING.put(129488, "20201001");
        DATA_MAPPING.put(128526, "20201001");
        DATA_MAPPING.put(128533, "20201001");
        DATA_MAPPING.put(128543, "20201001");
        DATA_MAPPING.put(128577, "20201001");
        DATA_MAPPING.put(128558, "20201001");
        DATA_MAPPING.put(129762, "20211115");
        DATA_MAPPING.put(129763, "20211115");
        DATA_MAPPING.put(129761, "20211115");
        DATA_MAPPING.put(129765, "20211115");
        DATA_MAPPING.put(129764, "20211115");
        DATA_MAPPING.put(129401, "20211115");
        DATA_MAPPING.put(128559, "20201001");
        DATA_MAPPING.put(128562, "20201001");
        DATA_MAPPING.put(129299, "20201001");
        DATA_MAPPING.put(128563, "20201001");
        DATA_MAPPING.put(129402, "20201001");
        DATA_MAPPING.put(128551, "20201001");
        DATA_MAPPING.put(128552, "20201001");
        DATA_MAPPING.put(128550, "20201001");
        DATA_MAPPING.put(128560, "20201001");
        DATA_MAPPING.put(128549, "20201001");
        DATA_MAPPING.put(128557, "20201001");
        DATA_MAPPING.put(128553, "20201001");
        DATA_MAPPING.put(128546, "20201001");
        DATA_MAPPING.put(128547, "20201001");
        DATA_MAPPING.put(128544, "20201001");
        DATA_MAPPING.put(128531, "20201001");
        DATA_MAPPING.put(128534, "20201001");
        DATA_MAPPING.put(129324, "20201001");
        DATA_MAPPING.put(128542, "20201001");
        DATA_MAPPING.put(128555, "20201001");
        DATA_MAPPING.put(128548, "20201001");
        DATA_MAPPING.put(129393, "20201001");
        DATA_MAPPING.put(128169, "20201001");
        DATA_MAPPING.put(128545, "20201001");
        DATA_MAPPING.put(128561, "20201001");
        DATA_MAPPING.put(128127, "20201001");
        DATA_MAPPING.put(128128, "20201001");
        DATA_MAPPING.put(128125, "20201001");
        DATA_MAPPING.put(128520, "20201001");
        DATA_MAPPING.put(129313, "20201001");
        DATA_MAPPING.put(128123, "20201001");
        DATA_MAPPING.put(129302, "20201001");
        DATA_MAPPING.put(128175, "20201001");
        DATA_MAPPING.put(128064, "20201001");
        DATA_MAPPING.put(127801, "20201001");
        DATA_MAPPING.put(127804, "20201001");
        DATA_MAPPING.put(127799, "20201001");
        DATA_MAPPING.put(127797, "20201001");
        DATA_MAPPING.put(127821, "20201001");
        DATA_MAPPING.put(127874, "20201001");
        DATA_MAPPING.put(127751, "20210831");
        DATA_MAPPING.put(129473, "20201001");
        DATA_MAPPING.put(127911, "20210521");
        DATA_MAPPING.put(127800, "20210218");
        DATA_MAPPING.put(129440, "20201001");
        DATA_MAPPING.put(128144, "20201001");
        DATA_MAPPING.put(127789, "20201001");
        DATA_MAPPING.put(128139, "20201001");
        DATA_MAPPING.put(127875, "20201001");
        DATA_MAPPING.put(129472, "20201001");
        DATA_MAPPING.put(9749, "20201001");
        DATA_MAPPING.put(127882, "20201001");
        DATA_MAPPING.put(127880, "20201001");
        DATA_MAPPING.put(9924, "20201001");
        DATA_MAPPING.put(128142, "20201001");
        DATA_MAPPING.put(127794, "20201001");
        DATA_MAPPING.put(129410, "20210218");
        DATA_MAPPING.put(128584, "20201001");
        DATA_MAPPING.put(128148, "20201001");
        DATA_MAPPING.put(128140, "20201001");
        DATA_MAPPING.put(128152, "20201001");
        DATA_MAPPING.put(128159, "20201001");
        DATA_MAPPING.put(128158, "20201001");
        DATA_MAPPING.put(128147, "20201001");
        DATA_MAPPING.put(128149, "20201001");
        DATA_MAPPING.put(128151, "20201001");
        DATA_MAPPING.put(129505, "20201001");
        DATA_MAPPING.put(128155, "20201001");
        DATA_MAPPING.put(10084, "20210218");
        DATA_MAPPING.put(65039, "20210218");
        DATA_MAPPING.put(8205, "20210218");
        DATA_MAPPING.put(129657, "20210218");
        DATA_MAPPING.put(128156, "20201001");
        DATA_MAPPING.put(128154, "20201001");
        DATA_MAPPING.put(128153, "20201001");
        DATA_MAPPING.put(129294, "20201001");
        DATA_MAPPING.put(129293, "20201001");
        DATA_MAPPING.put(128420, "20201001");
        DATA_MAPPING.put(128150, "20201001");
        DATA_MAPPING.put(128157, "20201001");
        DATA_MAPPING.put(127873, "20211115");
        DATA_MAPPING.put(129717, "20211115");
        DATA_MAPPING.put(127942, "20211115");
        DATA_MAPPING.put(127838, "20210831");
        DATA_MAPPING.put(128240, "20201001");
        DATA_MAPPING.put(128302, "20201001");
        DATA_MAPPING.put(128081, "20201001");
        DATA_MAPPING.put(128055, "20201001");
        DATA_MAPPING.put(129412, "20210831");
        DATA_MAPPING.put(127771, "20201001");
        DATA_MAPPING.put(129420, "20201001");
        DATA_MAPPING.put(129668, "20210521");
        DATA_MAPPING.put(128171, "20201001");
        DATA_MAPPING.put(128049, "20201001");
        DATA_MAPPING.put(129409, "20201001");
        DATA_MAPPING.put(128293, "20201001");
        DATA_MAPPING.put(128038, "20210831");
        DATA_MAPPING.put(129415, "20201001");
        DATA_MAPPING.put(129417, "20210831");
        DATA_MAPPING.put(127752, "20201001");
        DATA_MAPPING.put(128053, "20201001");
        DATA_MAPPING.put(128029, "20201001");
        DATA_MAPPING.put(128034, "20201001");
        DATA_MAPPING.put(128025, "20201001");
        DATA_MAPPING.put(129433, "20201001");
        DATA_MAPPING.put(128016, "20210831");
        DATA_MAPPING.put(128060, "20201001");
        DATA_MAPPING.put(128040, "20201001");
        DATA_MAPPING.put(129445, "20201001");
        DATA_MAPPING.put(128059, "20210831");
        DATA_MAPPING.put(128048, "20201001");
        DATA_MAPPING.put(129428, "20201001");
        DATA_MAPPING.put(128054, "20211115");
        DATA_MAPPING.put(128041, "20211115");
        DATA_MAPPING.put(128047, "20220110");
        DATA_MAPPING.put(129437, "20211115");
        DATA_MAPPING.put(128039, "20211115");
        DATA_MAPPING.put(128012, "20210218");
        DATA_MAPPING.put(128045, "20201001");
        DATA_MAPPING.put(128031, "20210831");
        DATA_MAPPING.put(127757, "20201001");
        DATA_MAPPING.put(127774, "20201001");
        DATA_MAPPING.put(127775, "20201001");
        DATA_MAPPING.put(11088, "20201001");
        DATA_MAPPING.put(127772, "20201001");
        DATA_MAPPING.put(129361, "20201001");
        DATA_MAPPING.put(127820, "20211115");
        DATA_MAPPING.put(127827, "20210831");
        DATA_MAPPING.put(127819, "20210521");
        DATA_MAPPING.put(127818, "20211115");
    }

    String s0 = "\uD83C\uDF1A\uD83C\uDF1A";

    public static boolean checkUrl(String u0) {
        try {
            URL url = new URL(u0);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getInputStream();
            return 200 == connection.getResponseCode();
        } catch (IOException e) {
            return false;
        }
    }

    public static String toEmoji(String s1) {
        char c0 = s1.charAt(0);
        char c1 = s1.charAt(1);
        Integer e0 = c0 - (MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >> 10));
        e0 = e0 << 10;
        Integer e1 = c1 - MIN_LOW_SURROGATE;
        Integer e = e0 + e1;
        String s = Integer.toString(e, 16);
        return s;
    }

    public static Integer toEmojiValue(String s1) {
        char c0 = s1.charAt(0);
        char c1 = s1.charAt(1);
        Integer e0 = c0 - (MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >> 10));
        e0 = e0 << 10;
        Integer e1 = c1 - MIN_LOW_SURROGATE;
        Integer e = e0 + e1;
        return e;
    }

    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
        String mess = MessageSerializer.messageChain2String(event.getMessage());
        if (mess.matches(P0)) {
            try {
                String[] ss = mess.split("");
                String s1 = ss[0] + ss[1];
                String s2 = ss[2] + ss[3];
                String e1 = toEmoji(s1);
                String e2 = toEmoji(s2);
                String date = "20201001";
                Integer i1 = toEmojiValue(s1);
                Integer i2 = toEmojiValue(s2);
                if (DATA_MAPPING.containsKey(i1.intValue())) {
                    date = DATA_MAPPING.get(i1.intValue());
                } else if (DATA_MAPPING.containsKey(i2.intValue())) {
                    date = DATA_MAPPING.get(i2.intValue());
                }
                String url0;
                url0 = String.format(U0, date, e1, e1, e2);
                if (!checkUrl(url0)) {
                    String e0 = e1;
                    e1 = e2;
                    e2 = e0;
                    url0 = String.format(U0, date, e1, e1, e2);
                }
                if (!checkUrl(url0)) {
//                    MessageTools.instance.sendMessageInGroupWithAt("合成失败了", event.getSubject().getId(), event.getSender().getId());
                } else {
                    MessageUtils.INSTANCE.sendMessageInGroupWithAt(Tool.INSTANCE.pathToImg(url0), event.getSubject().getId(), event.getSender().getId());
                }
            } catch (Exception e) {
//                MessageTools.instance.sendMessageInGroupWithAt("合成失败,呜呜", event.getSubject().getId(), event.getSender().getId());
            }
        }
    }
}
