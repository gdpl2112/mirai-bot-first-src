package Project.utils;

import Project.controllers.auto.ControllerSource;

/**
 * @author github.kloping
 */
public class KlopingWebDataBaseInteger implements KlopingWebDataBase<Integer> {
    private final String pwd;
    private final Integer defaultValue;

    public KlopingWebDataBaseInteger(String pwd, Integer defaultValue) {
        this.pwd = pwd;
        this.defaultValue = defaultValue;
    }

    @Override
    public Integer getValue(Object key) {
        String v = ControllerSource.klopingWeb.get(key.toString(), pwd);
        if (v == null || v.trim().isEmpty()) {
            ControllerSource.klopingWeb.put(key.toString(), defaultValue.toString(), pwd);
            return defaultValue;
        }
        return Integer.valueOf(v);
    }

    @Override
    public Integer setValue(Object key, Integer value) {
        String v = ControllerSource.klopingWeb.put(key.toString(), value.toString(), pwd);
        if (v == null || v.trim().isEmpty()) {
            return defaultValue;
        } else {
            return Integer.valueOf(v);
        }
    }
}
