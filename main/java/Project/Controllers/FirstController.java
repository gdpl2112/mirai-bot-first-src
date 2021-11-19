package Project.Controllers;

import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.MySpringTool.annotations.Controller;

@Controller
public class FirstController {

//    @Action("测试")
//    public void a(Group g) {
//        SimpleServiceMessage message = new SimpleServiceMessage(1, Tool.getStringFromFile("./test/b.xml"));
//        Resource.bot.getGroup(g.getId()).sendMessage(message);
//    }
//
//    @Action("测试.+")
//    public String setIcon(@AllMess String message, Group group, User qq) {
//        String img = MessageTools.getImageUrlFromMessageString(message);
//        if (img == null)
//            return ("请携带图片");
//        if (!Downloader.DownloadPicture(img, "./temp/1/" + UUID.randomUUID() + ".png"))
//            return ("图片上传失败..请更换图片重试");
//        return "11";
//    }
}
