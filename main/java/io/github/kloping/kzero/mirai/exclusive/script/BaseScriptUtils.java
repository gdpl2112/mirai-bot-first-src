package io.github.kloping.kzero.mirai.exclusive.script;

import com.alibaba.fastjson.JSONObject;
import io.github.kloping.kzero.main.api.MessageSerializer;
import io.github.kloping.map.MapUtils;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BaseScriptUtils implements ScriptUtils {

    public static final Map<Long, Map<String, Object>> BID_2_VARIABLES = new HashMap<>();

    public static final Map<Long, ScriptEngine> BID_2_SCRIPT_ENGINE = new HashMap<>();

    public static final <T, K1, K2> T getValueOrDefault(Map<K1, Map<K2, T>> map, K1 k1, K2 k2, T def) {
        if (map.containsKey(k1)) {
            Map<K2, T> m2 = map.get(k1);
            if (m2.containsKey(k2)) {
                return m2.get(k2);
            } else {
                m2.put(k2, def);
                map.put(k1, m2);
                return def;
            }
        } else {
            Map<K2, T> m2 = new HashMap<>();
            m2.put(k2, def);
            map.put(k1, m2);
            return def;
        }
    }

    private long bid;
    private RestTemplate template;
    private MessageSerializer<Message > serializer;

    public BaseScriptUtils(long bid, RestTemplate template, MessageSerializer<Message > serializer) {
        this.bid = bid;
        this.template = template;
        this.serializer = serializer;
    }

    @Override
    public String requestGet(String url) {
        return template.getForObject(url, String.class);
    }

    @Override
    public String requestPost(String url, String data) {
        return template.postForObject(url, data, String.class);
    }

    @Override
    public String serialize(Message  chain) {
        return serializer.serialize(chain);
    }

    @Override
    public Object get(String name) {
        return getValueOrDefault(BID_2_VARIABLES, bid, name, null);
    }

    @Override
    public Object set(String name, Object value) {
        Object ov = getValueOrDefault(BID_2_VARIABLES, bid, name, null);
        MapUtils.append(BID_2_VARIABLES, bid, name, value, HashMap.class);
        return ov;
    }

    @Override
    public Integer clear() {
        int i = 0;
        Map<String, Object> sizeMap = BID_2_VARIABLES.get(bid);
        if (sizeMap != null) {
            i = sizeMap.size();
            sizeMap.clear();
        }
        return i;
    }

    @Override
    public Object del(String name) {
        Map<String, Object> sizeMap = BID_2_VARIABLES.get(bid);
        if (sizeMap != null) {
            Object oa = sizeMap.get(name);
            sizeMap.remove(name);
            return oa;
        }
        return null;
    }

    @Override
    public List<Map.Entry<String, Object>> list() {
        if (BID_2_VARIABLES.containsKey(bid))
            return new LinkedList<>(BID_2_VARIABLES.get(bid).entrySet());
        return new ArrayList<>();
    }

    @Override
    public <T> T newObject(String name, Object... args) {
        try {
            Class cla = Class.forName(name);
            List<Class> list = new ArrayList<>();
            for (Object arg : args) {
                list.add(arg.getClass());
            }
            Constructor constructor = cla.getDeclaredConstructor(list.toArray(new Class[0]));
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:user-%s-db.db", bid));
            Statement statement = connection.createStatement();
            boolean k = statement.execute(sql);
            statement.close();
            connection.close();
            return k;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Object> executeSelectList(String sql) {
        JdbcTemplate template = getJdbcTemplate(bid);
        List<Map<String, Object>> list = template.queryForList(sql);
        if (list.size() == 0) return null;
        else {
            List out = new ArrayList();
            for (Map<String, Object> map : list) {
                JSONObject jo = new JSONObject();
                for (String key : map.keySet()) {
                    jo.put(key, map.get(key));
                }
                out.add(jo);
            }
            return out;
        }
    }

    @Override
    public Object executeSelectOne(String sql) {
        JdbcTemplate template = getJdbcTemplate(bid);
        List<Map<String, Object>> list = template.queryForList(sql);
        if (list.size() >= 0) {
            for (Map<String, Object> map : list) {
                JSONObject jo = new JSONObject();
                for (String key : map.keySet()) {
                    jo.put(key, map.get(key));
                }
                return jo;
            }
        }
        return null;
    }

    private static Map<Long, JdbcTemplate> templateMap = new HashMap<>();

    @NotNull
    private static JdbcTemplate getJdbcTemplate(long bid) {
        if (templateMap.containsKey(bid)) return templateMap.get(bid);
        DataSource dataSource = new AbstractDataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(String.format("jdbc:sqlite:user-%s-db.db", bid));
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return DriverManager.getConnection(String.format("jdbc:sqlite:user-%s-db.db", bid));
            }
        };
        JdbcTemplate template = new JdbcTemplate(dataSource);
        templateMap.put(bid, template);
        return template;
    }

    @Override
    public void newGlobal() {
        BID_2_SCRIPT_ENGINE.remove(bid);
    }
}
