package Project.interfaces.http_api;

import io.github.kloping.MySpringTool.annotations.http.*;

/**
 * @author github kloping
 * @version 1.0
 */
@HttpClient("http://jiuli.xiaoapi.cn/i")
public interface JuiLi {
    /**
     * 举牌子的
     * <a href="http://jiuli.xiaoapi.cn/i/xiaoren_jupai.php?msg=">api</a>
     *
     * @param msg
     * @return
     */
    @GetPath("xiaoren_jupai.php")
    @CookieFrom("this")
    byte[] jupaizi(@ParamName("msg") String msg);

    /**
     * 爬的
     * <a href="http://jiuli.xiaoapi.cn/i/pa_img.php?qq=3474006766&id=0">api</a>
     *
     * @param msg
     * @param id
     * @return
     */
    @GetPath("pa_img.php")
    @CookieFrom("this")
    byte[] paImg(@ParamName("qq") Long qq,
                 @DefaultValue("0") @ParamName("id") Integer id);
}
