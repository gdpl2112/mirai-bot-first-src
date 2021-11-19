package Project.Controllers.Plugins;


import io.github.kloping.MySpringTool.annotations.Controller;

import static io.github.kloping.Mirai.Main.Resource.println;

@Controller
public class MediaRecommender {
    public MediaRecommender() {
        println(this.getClass().getSimpleName() + "构建");
    }

/*
    @Before
    public void before(Entitys.Group group) throws NoRunException {
        if (!AllK)
            throw new NoRunException();
        if (!CanGroup(group.getId())) {
            throw new NoRunException();
        }
    }

    @Action(value = "快手短视频", otherName = {"快手视频"})
    public String a(User user) {
        try {
            List<MediaSource> list = MediaGetter.getOne_KS(null);
            map.put(user.getId(), list);
            String[] sss = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                sss[i] = list.get(i).getImgUrl();
            return "使用,播放第n的" + Tool.pathToImg(Drawer.drawImages2Image(sss));
        } catch (Exception e) {
            e.printStackTrace();
            return "未知异常";
        }
    }

    @Action(value = "快手搜索<.+=>name>")
    public String b(@Param("name") String name, User user) {
        try {
            List<MediaSource> list = MediaGetter.getOne_KS(name);
            map.put(user.getId(), list);
            String[] sss = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                sss[i] = list.get(i).getImgUrl();
            return "使用,播放第n的" + Tool.pathToImg(Drawer.drawImages2Image(sss));
        } catch (Exception e) {
            e.printStackTrace();
            return "未知异常";
        }
    }

    @Action(value = "哔哩哔哩搜索<.+=>name>", otherName = {"哔哩搜索<.+=>name>"})
    public String c(@Param("name") String name, User user) {
        try {
            List<MediaSource> list = MediaGetter.getOne_Bili(name);
            map.put(user.getId(), list);
            String[] sss = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                sss[i] = list.get(i).getImgUrl();
            return "使用,播放第n的" + Tool.pathToImg(Drawer.drawImages2Image(sss));
        } catch (Exception e) {
            e.printStackTrace();
            return "未知异常";
        }
    }

    public static final Map<Long, List<MediaSource>> map = new ConcurrentHashMap<>();

    @Action("播放第<.+=>st>")
    public String d(@Param("st") String num, User user) {
        Integer st = 1;
        try {
            st = Integer.valueOf(num.replaceAll("个", "").trim());
        } catch (Exception e) {
        }
        if (!map.containsKey(user.getId())) return "您还未进行相关搜索";
        try {
            MediaSource source = map.get(user.getId()).get(st - 1);
            return source.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "未知异常";
        }
    }*/
}



