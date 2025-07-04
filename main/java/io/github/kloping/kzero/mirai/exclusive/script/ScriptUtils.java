package io.github.kloping.kzero.mirai.exclusive.script;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;

import java.util.List;
import java.util.Map;

/**
 * 脚本工具
 * 预设脚本变量为:
 * utils
 *
 * @author github.kloping
 */
public interface ScriptUtils {

    /**
     * get 请求
     *
     * @param url
     * @return
     */
    String requestGet(String url);

    /**
     * post 请求
     *
     * @param url
     * @return
     */
    String requestPost(String url, String data);


    /**
     * 查询image url 通过image id
     *
     * @param imageId
     * @return
     */
    default String queryUrlFromId(String imageId) {
        return Image.queryUrl(Image.fromId(imageId));
    }

    /**
     * 正向解析mes为定制string
     *
     * @param chain
     * @return
     */
    String serialize(Message  chain);


    /**
     * 获取变量
     *
     * @param name
     * @return
     */
    Object get(String name);

    /**
     * 设置变量
     *
     * @param name
     * @param value
     * @return
     */
    Object set(String name, Object value);

    /**
     * 清除当前账号的所有变量
     *
     * @return
     */
    Integer clear();

    /**
     * 删除指定变量
     *
     * @param name
     * @return
     */
    Object del(String name);

    /**
     * 列出当前bot所有变量
     *
     * @return
     */
    List<Map.Entry<String, Object>> list();

    /**
     * js 创建Java的对象
     *
     * @param name 类全名 如: java.util.HashMap
     * @param args 参数可选
     * @param <T>
     * @return
     */
    <T> T newObject(String name, Object... args);

    /**
     * 基于自己bot执行一句sql
     * (通用 只返回 Boolean)
     *
     * @param sql
     * @return
     */
    boolean executeSql(String sql);

    /**
     * 执行一句查询sql 返回 list obj
     *
     * @param sql
     * @return
     */
    List<Object> executeSelectList(String sql);

    /**
     * 执行一句查询sql 返回 单个对象
     *
     * @param sql
     * @return
     */
    Object executeSelectOne(String sql);

    /**
     * 启用新全局
     */
    void newGlobal();
}
