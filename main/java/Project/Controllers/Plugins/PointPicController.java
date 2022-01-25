package Project.Controllers.Plugins;

import Entitys.Group;
import Entitys.User;
import Project.detailPlugin.SearchPic;
import Project.ResourceSet;
import Project.Tools.Tool;
import io.github.kloping.MySpringTool.annotations.*;
import io.github.kloping.MySpringTool.exceptions.NoRunException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.Controllers.ControllerTool.opened;
import static Project.DataBases.DataBase.isFather;
import static Project.Tools.Tool.isIlleg;
import static io.github.kloping.Mirai.Main.Resource.println;

/**
 * @author github-kloping
 */
@Controller
public class PointPicController {
    public PointPicController() {
        println(this.getClass().getSimpleName() + "构建");
    }

    @Before
    public void before(Entitys.Group group) throws NoRunException {
        if (!opened(group.getId(), this.getClass())) {
            throw new NoRunException("未开启");
        }
    }

    public static final Map<Long, String[]> PIC_HISTORY = new ConcurrentHashMap<>();

    @Action("发张<.+=>name>")
    public Object sendImg(@Param("name") String name, Group group) {
        if (isIlleg(name)) return ResourceSet.FinalString.IS_ILLEGAL_TIPS_1;
        return Tool.pathToImg(getImgFromName(name));
    }

    private static final String SOUTUU0 = "https://api.iyk0.com/swt/?msg=";

    public static final String getImgFromName(String name) {
        try {
            name = URLEncoder.encode(name);
            String url = SOUTUU0 + name;
            URL u = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
            String end = "";
            System.out.println(end = br.readLine());
            if (end == null || end.isEmpty()) {
                end = getImgFromName(name);
            }
            br.close();
            return end;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @AutoStand
    SearchPic searchPic;

    @Action("百度搜图<.+=>name>")
    public String searchPic(@Param("name") String name, User user) {
        if (isIlleg(name)) {
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
    public String searchPicM(@Param("name") String name, User user) {
        if (isIlleg(name)) {
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
    public String searchPic2(@Param("name") String name, User user) {
        if (isIlleg(name)) {
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
    public Object sendSt(@Param("str") String str, Group group, User user) {
        str = str.replaceAll("个|张", "");
        if (!PIC_HISTORY.containsKey(user.getId())) {
            return "您还没有进行相关";
        }
        if ("全部".equals(str) && isFather(user.getId())) {
            String[] ss = PIC_HISTORY.get(user.getId());
            Object[] objects = new Object[ss.length];
            for (int n = 0; n < ss.length; n++) {
                objects[n] = Tool.pathToImg(ss[n]);
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
                Integer n1 = Integer.valueOf(Tool.findNumberFromString(str));
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
                    sb.append(Tool.pathToImg(strings[n - 1])).append("\r\n");
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

    public static final Pattern PATTERN = Pattern.compile("http[a-zA-Z0-9/:.]*");

    private static String getUrl(String url) {
        try {
            Matcher matcher = PATTERN.matcher(url);
            if (matcher.find()) {
                url = matcher.group().trim();
            }
        } catch (Exception e) {

        }
        return url.trim();
    }

    @Action("解析快手图片<.+=>str>")
    public String parseKs(@Param("str") String urlStr, User user) {
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
    public String parseDy(@Param("str") String urlStr, User user) {
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
