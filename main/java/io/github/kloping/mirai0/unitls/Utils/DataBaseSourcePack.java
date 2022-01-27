package io.github.kloping.mirai0.unitls.Utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;

import static io.github.kloping.mirai0.Main.Resource.contextManager;

/**
 * @author github-kloping
 */
public class DataBaseSourcePack {
    private static DruidDataSource ds;
    private static String url, user, password;
    private static Connection connection = null;

    private static synchronized void init() {
        try {
            ds = new DruidDataSource();
            url = contextManager.getContextEntity(String.class, "mysql.url");
            user = contextManager.getContextEntity(String.class, "mysql.user");
            password = contextManager.getContextEntity(String.class, "mysql.password");
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(password);
            ds.setInitialSize(3);
            ds.setMaxActive(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Connection getConnection() {
        try {
            if (ds == null) {
                init();
            }
            return connection == null || connection.isClosed() ? connection = ds.getConnection() : connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
