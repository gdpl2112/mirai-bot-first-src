import Project.dataBases.SourceDataBase;
import io.github.kloping.MySpringTool.annotations.Action;
import io.github.kloping.MySpringTool.annotations.AllMess;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.BotStarter;
import io.github.kloping.mirai0.unitls.drawers.GameDrawer;
import io.github.kloping.mirai0.unitls.drawers.entity.GameMap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static Project.controllers.recr.HasTimeActionController.ID2JL;

/**
 * @author github.kloping
 */
public class TestBootstrap {

    public static void main(String[] args) throws Throwable {
        System.out.println(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
        FTPTools.upload("192.168.1.103", 21, "root", "443212", "/", new ByteArrayInputStream(FileUtils.getBytesFromFile("D:\\Projects\\OwnProjects\\MyMirai_01\\src\\M1.jar")), "/root/projects/website0/M1.jar");
        BotStarter.main(args);
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) {
//            int r = scanner.nextInt();
//            extracted(r);
//            System.out.println("ok");
//        }
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

    private static void extracted(int n) throws Exception {
        GameMap.GameMapBuilder builder = new GameMap.GameMapBuilder();
        builder.setWidth(5).setHeight(3);
        int i = 0;
        List<Integer> list = new LinkedList<>(ID2JL.keySet());
        for (int i1 = 0; i1 < 5; i1++) {
            builder.append(i1 + 1, 1, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(5, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        for (int i1 = 5; i1 > 0; i1--) {
            builder.append(i1, 3, SourceDataBase.getImgPathById(list.get(i++), false));
        }
        builder.append(1, 2, SourceDataBase.getImgPathById(list.get(i++), false));
        String name = UUID.randomUUID() + ".gif";
        new File("./temp").mkdirs();
        File file = new File("./temp/" + name);
        AtomicInteger al = new AtomicInteger();
        Map<Integer, Integer> am = new HashMap<>();
        Map<Integer, Integer> ai = new HashMap<>();
        AtomicInteger st = new AtomicInteger();
        ID2JL.forEach((k, v) -> {
            for (Integer integer = 0; integer < v; integer++) {
                ai.put(al.get(), st.get());
                am.put(al.getAndIncrement(), k);
            }
            st.getAndIncrement();
        });
        GameDrawer.drawerDynamic(builder.build(), n, SourceDataBase.getImgPathById(101, false), file);
    }

    @Action(".*?\\[@me].*?")
    public Object s0(@AllMess String all) {
        return "???";
    }
}
