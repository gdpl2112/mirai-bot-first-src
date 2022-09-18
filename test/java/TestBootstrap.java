import Project.services.autoBehaviors.GhostBehavior;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.iwanna.buy.api.Player;
import io.github.kloping.iwanna.buy.impl.simple.SimplePlayer;
import io.github.kloping.iwanna.buy.impl.simple.SimpleSys;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.Main.Resource;
import io.github.kloping.mirai0.commons.GhostObj;
import io.github.kloping.mirai0.unitls.Tools.Tool;

import java.io.File;

/**
 * @author github.kloping
 */
public class TestBootstrap {
    private static SimpleSys sys;

    public static void main(String[] args) throws Throwable {
        BotStarter.main(args);


//        KlopingWeb kloping = StarterApplication.Setting.INSTANCE.getContextManager().getContextEntity(KlopingWeb.class);
//        File file = new File("./temp/90f8bac2-4ce0-421d-bda7-dad3f43170ef.png");
//        byte[] bytes = FileUtils.getBytesFromFile(file.getAbsolutePath());
//        JSONObject object = new JSONObject();
//        object.put("data", bytes);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("content-type", "application/json");
//        String s0 = object.toString();
//        headers.put("content-length", String.valueOf(s0.length()));
//        headers.put("accept-encoding", "gzip,deflate");
//        headers.put("host", "localhost");
//        kloping.uploadImg(headers, "123", s0);
////        Document doc = Jsoup.connect("http://localhost/uploadImg?key=123").ignoreContentType(true)
////                .requestBody(s0).headers(headers).post();
//
////        System.out.println(doc);
//        System.out.println();
//        System.out.println(GameSkillDetailService.getAddP(8070, 207));
//        System.out.println(GameSkillDetailService.getAddP(8070, 206));
//        System.out.println(GameSkillDetailService.getAddP(8070, 205));
//        TestBootstrap testBootstrap = new TestBootstrap();
//        StarterApplication.Setting.INSTANCE.getContextManager().append(testBootstrap);
//        StarterApplication.Setting.INSTANCE.getActionManager().manager(testBootstrap);
//        System.err.println("all is ok");
//        System.out.println(getDuration(9));
//        Thread.sleep(2000L);
//        sys = SimpleSys.factory("./base");
//        SimpleSys.INSTANCE = sys;
//        sys.getShop().addListener(new OnEventListener() {
//            @Override
//            public void onEventBefore(Event event) {
//                send(event.getDesc());
//            }
//
//            @Override
//            public void onEventAfter(Event event) {
//
//            }
//        });
//        sys.addListener(new NextListener() {
//            @Override
//            public void onNexted(Sys sys) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("今日商品:\n");
//                int i = 1;
//                for (Commodity commodity : sys.getShop().all()) {
//                    sb.append(i++).append(".\"").append(commodity.getName()).append("\"当前价格:").append(commodity.getNowPrice()).append("\n");
//                }
//                send(sb.toString());
//            }
//
//            @Override
//            public void onNextBefore(Sys sys) {
//                send("昨日银行利率:" + sys.getBank().getInterestRate());
//            }
//        });
//        sys.getShop().map();
    }

    @Action(".*?\\[@me].*?")
    public Object s0(@AllMess String all) {
        return "???";
    }

    @Action("我的存款")
    public Object s0(long qid) {
        Player player = SimplePlayer.getInstance(qid, new File(sys.basePath(), sys.playersPath()));
        Number n0 = sys.getBank().selectMoney(player);
        if (n0 == null) n0 = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("现金:").append(player.getMoney()).append("\n")
                .append("银行存款:").append(n0);
        return sb.toString();

    }

    @Action("存钱.+")
    public Object s1(long qid, @AllMess String mess) {
        Integer i = Tool.tool.getInteagerFromStr(mess);
        if (i == null) return null;
        Player player = SimplePlayer.getInstance(qid, new File(sys.basePath(), sys.playersPath()));
        player.save(i);
        return s0(qid);
    }

    @Action("取钱.+")
    public Object s2(long qid, @AllMess String mess) {
        Integer i = Tool.tool.getInteagerFromStr(mess);
        if (i == null) return null;
        Player player = SimplePlayer.getInstance(qid, new File(sys.basePath(), sys.playersPath()));
        try {
            return sys.getBank().getMoney(player, i) ? "取钱成功" : "取钱失败";
        } finally {
            player.getSaver().apply(player);
        }
    }

    public static void send(Object o) {
        Resource.BOT.getGroup(759590727L).sendMessage(o.toString());
    }
}
