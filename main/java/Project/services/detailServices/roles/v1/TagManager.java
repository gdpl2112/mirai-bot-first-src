package Project.services.detailServices.roles.v1;

import io.github.kloping.map.MapUtils;
import Project.commons.gameEntitys.TagPack;

import java.util.*;

/**
 * @author github.kloping
 */
public class TagManager {
    public static final HashSet<TagPack> EMPTY_HASH_SET = new HashSet();
    private Long qid = -1L;
    private Map<String, Set<TagPack>> map = new HashMap<>();

    public TagManager(long qid) {
        this.qid = qid;
    }

    public synchronized Long getValue(String tag) {
        Long v = 0L;
        Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
        while (tagPackIterator.hasNext()) {
            TagPack tagPack = tagPackIterator.next();
            if (!tagPack.over()) {
                v += tagPack.getValue();
            } else {
                tagPackIterator.remove();
            }
        }
        return v;
    }

    public synchronized TagManager eddValue(String tag, Long v) {
        long v0 = v;
        Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
        while (tagPackIterator.hasNext()) {
            TagPack tagPack = tagPackIterator.next();
            if (!tagPack.over()) {
                long v1 = tagPack.getValue();
                if (v1 > v0) {
                    tagPack.eddValue(v0);
                } else {
                    v0 -= v1;
                    tagPackIterator.remove();
                }
            } else {
                tagPackIterator.remove();
            }
        }
        return this;
    }

    public synchronized TagManager eddValue(String tag) {
        Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
        while (tagPackIterator.hasNext()) {
            TagPack tagPack = tagPackIterator.next();
            tagPackIterator.remove();
        }
        return this;
    }

    public synchronized Boolean contains(String tag) {
       return getValue(tag)>0;
    }

    public synchronized void addTag(TagPack tagPack) {
        MapUtils.appendSet(map, tagPack.getTAG(), tagPack);
    }

    public void removeAll() {
        map.clear();
    }
}
