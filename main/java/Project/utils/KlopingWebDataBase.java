package Project.utils;

/**
 * @author github.kloping
 */
public interface KlopingWebDataBase<T> {
    /**
     * get A value
     *
     * @param key
     * @return
     */
    T getValue(Object key);

    /**
     * set A value
     *
     * @param key
     * @param t
     * @return old
     */
    T setValue(Object key, T t);
}
