package Project.Network;

import Entitys.Data;
import Entitys.UScore;
import Project.Controllers.NormalController.ScoreController;
import Project.DataBases.DataBase;
import Project.Tools.Tool;
import com.alibaba.fastjson.JSON;
import io.github.kloping.Mirai.Main.ITools.MessageTools;
import io.github.kloping.Mirai.Main.Resource;
import net.mamoe.mirai.contact.Group;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static Project.DataBases.DataBase.getAllInfo;

public class NetWorkMain {
    private static int port = 20042;

    public NetWorkMain(String ip) {

    }

    private static String token = "";
    private static ServerSocket server;

    public static void start() {
        System.err.println("token==>>" + (token = UUID.randomUUID().toString()));
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startClient();
    }

    private static Socket socket = null;

    private static void startClient() {
        try {
            System.out.println("parper ed");
            socket = server.accept();
            System.out.println("=============cliented===================");
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            while (socket.isConnected() && !socket.isClosed()) {
                String line = readLine(is);
                offer(line, os);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            start();
        }
    }

    public static synchronized String readLine(InputStream is) {
        try {
            byte b1 = -1;
            byte b2 = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                byte b = (byte) is.read();
                b2 = b1;
                b1 = b;
                if (b1 == 10 && b2 == 13) break;
                baos.write(b);
            }
            byte[] bytes = baos.toByteArray();
            return new String(bytes);
        } catch (Exception e) {
            System.err.println("读取失败,可能因为断开了连接");
        }
        return null;
    }

    private synchronized static void offer(String node, OutputStream os) throws Exception {
        Data data = JSON.parseObject(node, Data.class);
        System.err.println(data.toByteString() + "=>" + data);
        if (data.getToken().equals(token) || data.getToken().equals("kloping_")) {
            switch (data.getHeader()) {
                case "open":
                    open(Long.valueOf(data.toByteString()));
                    break;
                case "close":
                    close(Long.valueOf(data.toByteString()));
                    break;
                case "getSc":
                    getSc(os, Long.valueOf(data.toByteString()));
                    break;
                case "addSc":
                    addSc(os, data.toByteString());
                    break;
                case "send":
                    sendSc(os, Long.valueOf(data.toByteString()));
                    break;
            }
        } else {
            socket.close();
            throw new Exception("token 错误");
        }
    }

    private static void close(long v) {
        ScoreController.closeings.add(v);
    }

    private static void getSc(OutputStream os, long v) {
        UScore lll = getAllInfo(v);
        long v2 = lll.getScore_();
        OutputOffer(os, "score", v2 + "");
    }

    private static void addSc(OutputStream os, String vv) {
        String[] ss = vv.split("=>");
        long v = Long.valueOf(ss[0]);
        long w = Long.valueOf(ss[1]);
        DataBase.addScore_(v, w);
    }

    private static Group group = null;

    private static void sendSc(OutputStream os, long v) {
        if (group == null)
            group = Resource.bot.getGroup(278681553);
        long ui = getRand();
        if (group.contains(v)) {
            OutputOffer(os, "s1", ui + "");
            MessageTools.sendMessageInOneFromGroup("您当前正在注册账号,若没有请忽略此条消息\r\n您的验证码是:" + ui, v, 278681553);
        } else {
            OutputOffer(os, "s1", "-1");
        }
    }

    private static long getRand() {
        long[] ll = new long[6];
        ll[0] = Tool.rand.nextInt(10);
        ll[1] = Tool.rand.nextInt(10);
        ll[2] = Tool.rand.nextInt(10);
        ll[3] = Tool.rand.nextInt(10);
        ll[4] = Tool.rand.nextInt(10);
        ll[5] = Tool.rand.nextInt(10);
        String s1 = "" + ll[0] + ll[1] + ll[2] + ll[3] + ll[4] + ll[5];
        return Long.valueOf(s1);
    }

    public static void main(String[] args) {
        System.out.println(getRand());
        System.out.println(getRand());
        System.out.println(getRand());
        System.out.println(getRand());
    }

    private static void open(long v) {
        ScoreController.closeings.remove(v);
    }

    public static synchronized void OutputOffer(OutputStream os, String header, String mess) {
        try {
            Data data = new Data();
            data.setHeader(header);
            byte[] bytes = mess.getBytes(StandardCharsets.UTF_8);
            data.setBytes(bytes);
            data.setLen(bytes.length);
            data.setId(0);
            data.setToken("0");
            String dataStr = JSON.toJSONString(data);
            os.write(dataStr.getBytes(StandardCharsets.UTF_8));
            os.write(13);
            os.write(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
