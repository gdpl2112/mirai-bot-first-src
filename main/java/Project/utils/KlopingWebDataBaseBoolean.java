package Project.utils;

import Project.controllers.auto.ControllerSource;

/**
 * @author github.kloping
 */
public class KlopingWebDataBaseBoolean implements KlopingWebDataBase<Boolean> {
    private final String pwd;
    private final Boolean defaultValue;

    public KlopingWebDataBaseBoolean(String pwd, Boolean defaultValue) {
        this.pwd = pwd;
        this.defaultValue = defaultValue;
    }

    @Override
    public Boolean getValue(Object key) {
        String v = ControllerSource.klopingWeb.get(key.toString(), pwd);
        if (v == null || v.trim().isEmpty()) {
            ControllerSource.klopingWeb.put(key.toString(), defaultValue.toString(), pwd);
            return defaultValue;
        }
        return Boolean.valueOf(v);
    }

    @Override
    public Boolean setValue(Object key, Boolean value) {
        String v = ControllerSource.klopingWeb.put(key.toString(), value.toString(), pwd);
        if (v == null || v.trim().isEmpty()) {
            return defaultValue;
        } else {
            return Boolean.valueOf(v);
        }
    }
}
