package Project.controllers.plugins;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import Project.commons.rt.ResourceSet;
import Project.interfaces.httpApi.old.ApiIyk0;
import Project.plugins.SearchPic;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;
import Project.utils.Tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.commons.rt.ResourceSet.FinalNormalString.ALL_STR;
import static Project.commons.rt.ResourceSet.FinalValue.NOT_OPEN_NO_RUN_EXCEPTION;
import static Project.controllers.auto.ControllerTool.opened;
import static io.github.kloping.mirai0.Main.BootstarpResource.println;

/**
 * @author github-kloping
 */
@Controller
public class PointPicController {
    public static final Map<Long, String[]> PIC_HISTORY = new ConcurrentHashMap<>();
    public static final Pattern PATTERN = Pattern.compile("http[a-zA-Z0-9/:.]*");
    @AutoStand
    ApiIyk0 apiIyk0;
    @AutoStand
    SearchPic searchPic;

    public PointPicController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    public final static String getUrl(String url) {
        try {
            Matcher matcher = PATTERN.matcher(url);
            if (matcher.find()) {
                url = matcher.group().trim();
            }
        } catch (Exception e) {

        }
        return url.trim();
    }

    @Before
    public void before(SpGroup group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw NOT_OPEN_NO_RUN_EXCEPTION;
        }
    }

    @Action("百度搜图<.+=>name>")
    public String searchPic(@Param("name") String name, SpUser user) {
        if (Tool.INSTANCE.isIlleg(name)) {
            return ResourceSet.FinalString.IS_ILLEGAL_TIPS_1;
        }
        try {
            String[] strings = searchPic.getPicM(name);
            PIC_HISTORY.remove(user.getId());
            PIC_HISTORY.put(user.getId(), strings);
            return String.format("一共搜索到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        } catch (Exception e) {
            e.printStackTrace();
            return "搜索异常";
        }
    }

    @Action("搜图<.+=>name>")
    public String searchPicM(@Param("name") String name, SpUser user) {
        if (Tool.INSTANCE.isIlleg(name)) {
            return ResourceSet.FinalString.IS_ILLEGAL_TIPS_1;
        }
        try {
            String[] strings = searchPic.getPic(name);
            PIC_HISTORY.remove(user.getId());
            PIC_HISTORY.put(user.getId(), strings);
            return String.format("一共搜索到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        } catch (Exception e) {
            e.printStackTrace();
            return "搜索异常";
        }
    }

    @Action("堆糖搜图<.+=>name>")
    public String searchPic2(@Param("name") String name, SpUser user) {
        if (Tool.INSTANCE.isIlleg(name)) {
            return ResourceSet.FinalString.IS_ILLEGAL_TIPS_1;
        }
        try {
            String[] strings = searchPic.getPicDt(name);
            PIC_HISTORY.remove(user.getId());
            PIC_HISTORY.put(user.getId(), strings);
            return String.format("一共搜索到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        } catch (Exception e) {
            e.printStackTrace();
            return "搜索异常";
        }
    }

    @Action("发第<.+=>str>")
    public Object sendSt(@Param("str") String str, SpGroup group, SpUser user) {
        str = str.replaceAll("个|张", "");
        if (!PIC_HISTORY.containsKey(user.getId())) {
            return ResourceSet.FinalString.ILLEGAL_OPERATION;
        }
        if (ALL_STR.equals(str)) {
            String[] ss = PIC_HISTORY.get(user.getId());
            Object[] objects = new Object[ss.length];
            for (int n = 0; n < ss.length; n++) {
                objects[n] = Tool.INSTANCE.pathToImg(ss[n]);
            }
            return objects;
        }
        List<Integer> ns = new ArrayList<>();
        if (str.contains(",")) {
            String[] ss = str.split(",");
            for (String s : ss) {
                try {
                    Integer n1 = Integer.valueOf(s);
                    ns.add(n1);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        } else {
            try {
                Integer n1 = Integer.valueOf(Tool.INSTANCE.findNumberFromString(str));
                ns.add(n1);
            } catch (NumberFormatException e) {
                ns.add(1);
            }
        }
        String[] strings = PIC_HISTORY.get(user.getId());
        try {
            StringBuilder sb = new StringBuilder();
            for (int n : ns) {
                try {
                    sb.append(Tool.INSTANCE.pathToImg(strings[n - 1])).append("\r\n");
                } catch (Exception e) {
                    continue;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("一共搜索到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        }
    }

    @Action("解析快手图片<.+=>str>")
    public String parseKs(@Param("str") String urlStr, SpUser user) {
        try {
            String u1 = getUrl(urlStr);
            String[] strings = searchPic.parseKsImgs(u1);
            PIC_HISTORY.remove(user.getId());
            PIC_HISTORY.put(user.getId(), strings);
            return String.format("一共解析到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        } catch (Exception e) {
            e.printStackTrace();
            return "解析异常";
        }
    }

    @Action("解析抖音图片<.+=>str>")
    public String parseDy(@Param("str") String urlStr, SpUser user) {
        try {
            String[] strings = searchPic.parseDyImgs(getUrl(urlStr));
            PIC_HISTORY.remove(user.getId());
            PIC_HISTORY.put(user.getId(), strings);
            return String.format("一共解析到了:%s个结果\r\n用:发第(n1,n2)个", strings.length);
        } catch (Exception e) {
            e.printStackTrace();
            return "解析异常";
        }
    }
}
