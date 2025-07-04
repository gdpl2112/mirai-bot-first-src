package io.github.kloping.kzero.mirai.exclusive.script;

import io.github.kloping.spt.impls.PackageScannerImpl;
import io.github.kloping.spt.interfaces.component.PackageScanner;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;

import javax.script.ScriptEngine;

/**
 * script 交互对象
 * 预设脚本环境变量: context
 *
 * @author github.kloping
 */
public interface ScriptContext {
    /**
     * 获取bot
     *
     * @return
     */
    Bot getBot();

    /**
     * 获取元数据
     *
     * @return
     */
    MessageChain getRaw();

    /**
     * 发送字符串
     *
     * @param str
     */
    void send(String str);

    /**
     * 发送至所在环境
     *
     * @param message
     */
    void send(Message message);

    /**
     * 获得一个 Builder
     *
     * @return
     */
    default MessageChainBuilder builder() {
        return new MessageChainBuilder();
    }

    /**
     * 获得一个 转发行
     *
     * @return
     */
    ForwardMessageBuilder forwardBuilder();

    /**
     * 创建音乐分享消息
     *
     * @param kind
     * @param title
     * @param summer
     * @param jumUrl
     * @param picUrl
     * @param url
     * @return
     */
    default MusicShare createMusicShare(String kind, String title, String summer, String jumUrl, String picUrl, String url) {
        return new MusicShare(MusicKind.valueOf(kind), title, summer, jumUrl, picUrl, url);
    }

    /**
     * 上传图片
     *
     * @param url
     * @return
     */
    Image uploadImage(String url);

    /**
     * 构建 文本
     *
     * @param text
     * @return
     */
    default PlainText newPlainText(String text) {
        return new PlainText(text);
    }

    default SuperFace toSuperFace(int id) {
        return SuperFace.from(new Face(id));
    }

    /**
     * 反向 str 解析为 Message
     *
     * @param msg
     * @return
     */
    Message deSerialize(String msg);

    /**
     * 导入
     *
     * @param packages
     */
    default void imports(String... packages) {
        PackageScanner scanner = new PackageScannerImpl(true);
        ScriptEngine engine = BaseScriptUtils.BID_2_SCRIPT_ENGINE.get(getBot().getId());
        for (String aPackage : packages) {
            try {
                try {
                    Class cla = Class.forName(aPackage);
                    engine.put(cla.getSimpleName(), engine.eval("Java.type('" + cla.getName() + "')"));
                } catch (ClassNotFoundException e) {
                    for (Class<?> aClass : scanner.scan(this.getClass(), this.getClass().getClassLoader(), aPackage)) {
                        engine.put(aClass.getSimpleName(), engine.eval("Java.type('" + aClass.getName() + "')"));
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.err.println(aPackage);
            }
        }
    }
    /**
     * 发送者ID
     *
     * @return
     */
    User getSender();

    /**
     * 发送环境id 一般为 群id
     *
     * @return
     */
    Contact getSubject();

    /**
     * 所处环境 <a href="https://github.com/gdpl2112/dg-bot/blob/master/js-api.md"> 说明</a>
     *
     * @return
     */
    String getType();
}
