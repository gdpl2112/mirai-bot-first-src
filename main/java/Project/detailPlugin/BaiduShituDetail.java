package Project.detailPlugin;

import Project.interfaces.http_api.BaiduShitu0;
import Project.interfaces.http_api.IBaiduShitu;
import io.github.kloping.MySpringTool.annotations.AutoStand;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.mirai0.commons.apiEntitys.baiduShitu.BaiduShitu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class BaiduShituDetail {
    public static final Map<String, String> HEADERS = new HashMap<>();

    static {
        HEADERS.put("host", "graph.baidu.com");
        HEADERS.put("content-length", "343");
        HEADERS.put("accept", "*/*");
        HEADERS.put("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryhj2XGBn7j9a5lo36");
        HEADERS.put("origin", "https://image.baidu.com");
        HEADERS.put("x-requested-with", "idm.internet.download.manager.plus");
        HEADERS.put("sec-fetch-site", "same-site");
        HEADERS.put("sec-fetch-mode", "cors");
        HEADERS.put("sec-fetch-dest", "empty");
        HEADERS.put("referer", "https://image.baidu.com/?fr=shitu");
        HEADERS.put("accept-encoding", "gzip, deflate");
        HEADERS.put("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
    }

    @AutoStand
    static IBaiduShitu baiduShitu;

    @AutoStand
    static BaiduShitu0 baiduShitu0;

    public static String dataStr = null;

    public static BaiduShitu get(String imageUrl) {
        if (dataStr == null) {
            initDataStr();
        }
        BaiduShitu shitu = baiduShitu.get(HEADERS, "------WebKitFormBoundaryhj2XGBn7j9a5lo36\n" +
                "Content-Disposition: form-data; name=\"sdkParams\"\n" +
                "\n" +
                dataStr +
                "\n------WebKitFormBoundaryhj2XGBn7j9a5lo36--\n", null, null, null, null, imageUrl, System.currentTimeMillis());
        if (shitu.getData() == null) {
            initDataStr();
        }
        return shitu;
    }

    private static final Map<String, String> HEADER0 = new HashMap<>();

    private static void initDataStr() {
        HEADER0.put("host", "miao.baidu.com");
        HEADER0.put("content-length", "3936");
        HEADER0.put("accept", "*/*");
        HEADER0.put("content-type", "text/plain;charset=UTF-8");
        HEADER0.put("origin", "https://image.baidu.com");
        HEADER0.put("sec-fetch-site", "same-site");
        HEADER0.put("sec-fetch-mode", "cors");
        HEADER0.put("sec-fetch-dest", "empty");
        HEADER0.put("referer", "https://image.baidu.com/");
        HEADER0.put("accept-encoding", "gzip, deflate, br");
        HEADER0.put("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        dataStr = baiduShitu0.getDataSign(
                HEADER0, "eyJkYXRhIjoiODNkNTJjMGU5NDQ0NDNkZGUyM2ViYmU3MDg1YmZhMTM0ODM4ZTFlMTFlYmE5Y2JlMDU2M2JiNzI1MjE0NGE1OTI0NGM2NDM3YTFjZGFhNzQ0YzFiNzVlM2VhMDJjMTgzMmEwM2Y4M2NkNjFkY2ExNGUxY2M3YzA5ODMwOTYwMTY2YmZhNTEzYjZlYzEyMWQ1ODA0OGY0NTU5YTQ3NWQyZWZjZWExMjM2YjA2NDU2ZTNkM2I0NWJmOTRjM2Q3NjYzZDQ4MGYyOTY2ZTQ3N2ZiNGEyMDE5Zjc0MGM3MjEwYzNiYjFhM2Q5YjY1MjhmOGEzMjY3NTFiZDAxMDEzZGFmZWE3ZWVkMWNmZDhlNzhmYWY3MDNhNzMxNzQ5NzFkNGMxNGFlMzVhYWYyYWRmOTczM2RlOTg3NGYwZjQxZDY3NjRlZTk5MDA5Mjk4ZWVlY2RmN2I2YjQ2ZmQzNGQwY2M4Y2EzM2U0ZjIwYzFhZjE0NmNmZWVjNmY3NmI4NzY1ODJjMTcxYzZhNGQzNmUxYzM5NzBiNmNhNGU3NThiZTMxNTA1OTFiZjk4YzYzNzVjNjg0Y2E1OGY5ZWI4MDFmNTRhMTA1NmU5ZDY1MzEwYjViMWRlN2UzZGY3NTIwOWY4NmRiNjQ2YzI3NWMwZTIzOWE3OWY5YjBiOGY1MWU5NDA4ZjY3OTk1OTgyZGRjZmNmODVjODcwNDZhNjg1YjBhOTZlNmNkNjdhZjQ4N2EyYzExMTdiMTdkNzZlYzMyYWRiMzQ2M2I4OTE5NDE4MmMxYjhiNjhlMjZjYmRiZWY4NzVjM2UxZTI1ZWVjODA4NmJjNjU1NjZjZWYyMTQ5M2Y1YmQ5YjkzOTE1ZDAwNjU2NGM2NWRkYzYwMGI2NDFmZWI0ODAxYmM2ZDdjODBlMGU2MzdjYmE0MTMyM2MwOTgwNTI3MDE1OGMzMjI2OTFhN2U1OTMwMTZiYmM2OWQwYjA0ZTc1NjdkN2U1OGZmYjhjYjgyMGI1ODllZTQ5NmEyZTM2ODlhMDBhMjZjMTE4MTdlZDkwNGEyYjU3MDdkY2YyNzdiMmY0MDE4NmY1NGQyZjQ2M2Y3ZjQwNGU2NmVjMzg2YzM2YTlmY2RjZGQxYWE1NDBjNjliYzhjOTJmYTNiOWYwZjYwZGY5MWY4ZDZkODE5YmVhYmU3ZGM1NjgxY2IyMTRlMGNhODViMTI0ZDQ4YjlmYjFmMjk5NzZjNjA5NGI5ZmJlMDBhODg2MDdhYTNlZWUyZTFiMWY3OThmNDFkZDU5ZWYxYThkOTA4ZTNhMjhiZTNjMWRkMjAyY2U1ZGEwOTJmN2ZhMjZkYWU5NjI5YjdkYTU5N2QyZmYyMDQ3Nzk3YTk4ZGU5ODI1ZjZkMDkwMmY2MTIxZDU1NGFkZjQyN2ExYjFhYjZjYzliZjJmNWI1OGJjYzEzMWI1YTA2OWU3NzVlY2IwYmE2N2VhZWZkODMwZWNiMzhmYTQ5N2JhZGIxMjY0NmFhYmY5NDllYmI3YmJiMjMwMjE4MWMwZjljNDcyMzNmNTJhMTgwMDYzNzI4YzgwYzc1MjY0NGVkNTMxYjdhMmM1MzEwN2EwYTBhOTJlNWZkZmUxZGQ4OTRiZjY3ZmEwNjkyNmFjOWRjY2FjZGVjNTFiMWJiMjBiNTRlZjNhMjE5MDhkNjMyZjc0YjAyYWYyMGNhOGY5ZTc3MzliYzRhMTAyYTBkYjVkZmUxYWE4ZmNiMzM0OThkMzk3MDdiYjY1MzA4NWQ4NzNmM2I5OTA5OTIxYjQ5ZTg3NDUxOTAwNTQyZDY1OGU0NmU0ZjMyOGE3YjRlODdiOTlkYWUxOWUzYzNmNTQzMDY4MDQzMTJjNDA0YTc2ZjIxMTVkMDdlN2ViZTY4NDZlNzg0ZGE3MDAyZDdiNmI5ZjRjMzQ4MzhlOTdmMDFkZGEzMzgwMDIzMGMyZjQ0ZjZlZjFjOTE4ZDQ0YTQ2ZWRmMWNmYzYxODc2YzMzMGZjOWE3Yjk5ZWZmODk3ZmVmOTdiMjQwMmE2ZTIxMzk2MjdkMTNkM2RjYmMzNTMwNDg1Y2E5NWYxYTRjOWYxYjQ0OTgzOTUzNGFhNDRhNzdmYzhjNWQyN2RlZjI5OWM5MWYxNmJkODkyNTkwOTU5OTIwNzU5YmFiYTBhYjM0ZWU4MDJkNTdhODI0YmMwNGJkYWNmMjBjMDIzNmY2ZjdiMjM5MTc4MzcyYmFkODZiYzNlZjcxNzdhYzhlYjE4YWRlZTc1OWUzYTNhYzJjMTRiMjAzZDFjNGFiMWVlMTM2YzQyNzUyOGRiYWFiYWE3NjA3ZjZiYjkxYjM4ODQxN2M4ZmU5Y2ZmNjA2ZDljMDY1OWIxYjQ4MDAzMWY2MjExMjNkODRlZDBjY2ViOTkzYjJlYjQ5YTcxOWVhNjU0YzE2MGYyZjkyNzQwZTkzNGEyYzBiOGM0NDk5ZjA2YjcyMTU4YjE0MjI1NDJjOGM1YjlkM2Y3YzM5MTdkMmViYzQwY2MxMzk2ZmZiN2MxMDMxMmFjMTA3Y2UxMjljYjY1YzNmZmY3NDM3ZDY4OGFhM2M2ZTc2YTEwMzFkYWU1NDFhYzRhNWE3OTFjMGUzMTY4OTE5MTgzOWRlYzFhNWNjYzhiOGYzYWQ2ZTE2YTI1MjRlYWM2ODUyMGUyY2VlZmEyMWQyNzQwM2IyZTAwNjUwZTE1MWNmYmExMDdlM2UzY2MyNmI3MzUzZTAxNzQ3M2Q1MTBjNjUzMDVlZTM4YjY1NzNjYTQyYWY2MDQ2MzM0NjZiYjUyNGZlYTg0NmNjNmI5YjE0MTM1ZTVkODA3ZTMzMDZhOGQ5MzEzMmVlNDNmM2QzYzY5ZjYxYmExMGZlY2UxMDhhNGU5YTQwMDg0MmY2ZDg1NGQ5Y2UzNzcyZTk3MDJmYmM2ODgzNzcyMGI3NjBmOTM0MDZmY2Q1NmU3YmMxZWJmY2U3MDZiY2ZkYzUyZGJjNmM5ZDVlYmJkMWQxYjQwZTM1OTVmMDE4NDMxZWM3MjhkNjkyYTdiYTg2YjQ3Nzk1Y2RiNjdkNDlhM2ViMTYyZDU5YTU0Y2U2ZThkMzVjYzNhMzUxNjA2ZDY0YzliYzIyMGQyODVjMzMxYzU4YWU0NDBjZWIwNGExMjhmYWZiNTk0ZWU3ZjM1OWM4MjI5YjI5MDE2MjhlMzE2ZTkwYjc0YTM0MGM5ZDdhMjYxY2EzOGI1NzY0NGM0NGIzODQ3NzRkZWE0MGExOThlZmMwYWExNDY2YjMxMjU1NDg1ZjY4NjRjMjc1NWQ0NzY2ZmI0ODQ0NzE4MmM1OWE5ODE2YjBmYmYwZDRmZDIxYjYyMWI4YTg4YWVlNzM4ODRlMWVkMDBjYTg1OTZhODEwN2ZiYTA0ZDU4MTIxYmQyZDgzN2RkMTY1MDlmMGEzOWNkM2FlYzU4YWFiY2Q1MzY4Yjc1NjVjNzk2YzViNmFlZGU4ZDU0MWZiMzMxN2ViZmYwYjU4YjhhZjU3OGRjYjNkZjU3MTVhZDMxZmQ0NTFjNzIyYzAzMGVkY2JjZjA0YTQwMDZiNDFlYTY4OTZjZDZlYjg3NzIwOTBhNDE1MjRlMjBmZDIyYzU3YTMxMTRiYjZiMDMxZGE2NWE5NGRjNGIwNjZkOTM3NTRjYWExMTY0NDM5YTMxYWVlM2VlZjUyM2Q3MzQ1NjQ4ZjQ1MjRmZDYwMzhmNWIwOTdlYjUxODFlMmRmOTUzYWM3MWEyOWQ0ZTUwYzYyNThiODUzNTUxYTk0MDM4Y2NkN2ZlYzVkZjQxZTU3MjA5ZTA2M2M5M2FhOGZmZTY2OGZmZjhjNDJjZmJkZWJjMTJkZWNhNTA1MjhkZDM0ZTgwOWVlZjNmZjA5ZTc5ZDJkYmVlZDlhMmE1OGI0ODg1YTQzYjk4Y2YiLCJrZXlfaWQiOiJkMDUyYWU1MTMxYjk0NTZlIn0="
        );
    }
}
