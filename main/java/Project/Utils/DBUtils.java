package Project.Utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;

public class DBUtils {
    private static DruidDataSource ds;
    private static String url, user, password;
    private static Connection connection = null;

    static {
        try {
            ds = new DruidDataSource();
            url = "jdbc:mysql://139.198.186.159:3306/mydb1?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
            user = "root";
            password = "Han123456";
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
            return connection == null || connection.isClosed() ? connection = ds.getConnection() : connection;
        } catch (Exception e) {
            return null;
        }
    }
}
